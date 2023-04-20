package studio.stilip.geek.app.events.event

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import studio.stilip.geek.app.events.event.EventFragment.Companion.EVENT_ID
import studio.stilip.geek.data.UserCacheManager
import studio.stilip.geek.domain.entities.*
import studio.stilip.geek.domain.usecase.event.*
import studio.stilip.geek.domain.usecase.game.GetGameByIdUseCase
import studio.stilip.geek.domain.usecase.user.GetUserByIdUseCase
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(
    private val getEvent: GetEventByIdUseCase,
    private val getGameById: GetGameByIdUseCase,
    private val getMembersByEventId: GetMembersByEventIdUseCase,
    private val getUserById: GetUserByIdUseCase,
    private val subscribeToEvent: SubscribeToEventUseCase,
    private val unsubscribeFromEvent: UnsubscribeFromEventUseCase,
    private val getRoundsByEventId: GetRoundsByEventId2UseCase,
    private val getMembersScoresBySetId: GetMembersScoresBySetIdUseCase,
    private val getMemberScoreById: GetMemberScoreByIdUseCase,
    private val getSetById: GetSetByIdUseCase,
    private val createRound: CreateRoundNewUseCase,
    stateHandle: SavedStateHandle
) : ViewModel() {

    val userId = UserCacheManager.getUserId()
    val eventId: String = stateHandle[EVENT_ID]!!
    val currentRound = MutableStateFlow(RoundNew())

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

    val sets = currentRound.flatMapLatest { round ->
        flow {
            val sets = round.setsIds.map { id -> getSetById(id).first() }

            println("${sets.size}")

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
            subscribeToEvent(userId, eventId)
        }
    }

    fun onUnsubscribeClick() {
        viewModelScope.launch {
            unsubscribeFromEvent(userId, eventId)
        }
    }

    fun onRoundChanged(round: RoundNew) {
        if (round == currentRound.value) return
        viewModelScope.launch {
            currentRound.value = round
        }
    }

    fun onCreateClicked(title: String) {
        viewModelScope.launch {
            createRound(title, eventId)
        }
    }
}