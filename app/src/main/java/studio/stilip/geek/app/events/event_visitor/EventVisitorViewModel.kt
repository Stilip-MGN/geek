package studio.stilip.geek.app.events.event_visitor

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import studio.stilip.geek.app.events.event_visitor.EventVisitorFragment.Companion.EVENT_ID
import studio.stilip.geek.data.UserCacheManager
import studio.stilip.geek.domain.entities.*
import studio.stilip.geek.domain.usecase.event.*
import studio.stilip.geek.domain.usecase.game.GetGameByIdUseCase
import studio.stilip.geek.domain.usecase.user.GetUserByIdUseCase
import javax.inject.Inject

@HiltViewModel
class EventVisitorViewModel @Inject constructor(
    private val getEvent: GetEventByIdUseCase,
    private val getGameById: GetGameByIdUseCase,
    private val getMembersByEventId: GetMembersByEventIdUseCase,
    private val getUserById: GetUserByIdUseCase,
    private val subscribeToEvent: SubscribeToEventUseCase,
    private val unsubscribeFromEvent: UnsubscribeFromEventUseCase,
    private val getRoundsByEventId: GetRoundsByEventIdUseCase,
    private val getMembersScoresBySetId: GetMembersScoresBySetIdUseCase,
    private val getMemberScoreById: GetMemberScoreByIdUseCase,
    private val getSetById: GetSetByIdUseCase,
    stateHandle: SavedStateHandle
) : ViewModel() {

    val userId = UserCacheManager.getUserId()
    val eventId: String = stateHandle[EVENT_ID]!!
    private val currentRound = MutableStateFlow(Round())

    private val _membersId = getMembersByEventId(eventId)
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val event = getEvent(eventId)
        .stateIn(viewModelScope, SharingStarted.Eagerly, Event())

    val game = event.flatMapLatest { ev ->
        getGameById(ev.gameId)
    }.stateIn(viewModelScope, SharingStarted.Eagerly, Game())

    val members = _membersId.flatMapLatest { ids ->
        flow { emit(ids.map { id -> getUserById(id).first() }) }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val rounds = getRoundsByEventId(eventId)
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val sets = currentRound.flatMapLatest { current ->
        flow {
            if (current == Round())
                return@flow
            val sets = current.setsIds.map { id -> getSetById(id).first() }

            val list = sets.map { set ->
                val ids = getMembersScoresBySetId(set.id).first()
                val listMS = ids.map { id ->
                    getMemberScoreById(id).first()
                }
                val usersWithScore = listMS.map { memberScore ->
                    UserWithScore(
                        memberScore.id,
                        getUserById(memberScore.memberId).first(),
                        memberScore.score
                    )
                }
                SetWithList(
                    id = set.id,
                    title = set.title,
                    membersScores = usersWithScore
                )
            }
            emit(list)
        }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    fun onSubscribeClick() {
        viewModelScope.launch {
            if (members.value.count() < event.value.maxMembers)
                subscribeToEvent(userId, eventId)
        }
    }

    fun onUnsubscribeClick() {
        viewModelScope.launch {
            unsubscribeFromEvent(userId, eventId)
        }
    }

    fun onRoundChanged(round: Round) {
        if (round == currentRound.value)
            currentRound.value = Round()
        viewModelScope.launch {
            currentRound.value = round
        }
    }
}