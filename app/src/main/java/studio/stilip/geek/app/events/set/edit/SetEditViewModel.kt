package studio.stilip.geek.app.events.set.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import studio.stilip.geek.app.events.event.EventFragment.Companion.EVENT_ID
import studio.stilip.geek.app.events.event.EventFragment.Companion.ROUND_ID
import studio.stilip.geek.app.events.event.EventFragment.Companion.SET_ID
import studio.stilip.geek.domain.entities.MemberScore
import studio.stilip.geek.domain.entities.Set
import studio.stilip.geek.domain.entities.UserWithScore
import studio.stilip.geek.domain.usecase.event.*
import studio.stilip.geek.domain.usecase.user.GetUserByIdUseCase
import javax.inject.Inject

@HiltViewModel
class SetEditViewModel @Inject constructor(
    private val getMembersByEventId: GetMembersByEventIdUseCase,
    private val getUserById: GetUserByIdUseCase,
    private val getSetById: GetSetByIdUseCase,
    private val getMembersScoresBySetId: GetMembersScoresBySetIdUseCase,
    private val getMemberScoreById: GetMemberScoreByIdUseCase,
    private val updateSet: UpdateSetUseCase,
    private val deleteSet: DeleteSetUseCase,
    stateHandle: SavedStateHandle
) : ViewModel() {

    val membersScores = MutableStateFlow(emptyList<UserWithScore>())
    val setUpdatedOrDeleted = MutableStateFlow<Boolean?>(null)
    private val _setTitle = MutableStateFlow("")
    private val membersScoresDeleted = arrayListOf<UserWithScore>()

    val eventId: String = stateHandle[EVENT_ID]!!
    private val roundId: String = stateHandle[ROUND_ID]!!
    private val setId: String = stateHandle[SET_ID]!!

    private val _membersId = getMembersByEventId(eventId)
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val members = _membersId.flatMapLatest { ids ->
        flow { emit(ids.map { id -> getUserById(id).first() }) }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val set = getSetById(setId)
        .stateIn(viewModelScope, SharingStarted.Eagerly, Set())

    private val _memberScoreIds = getMembersScoresBySetId(setId)
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    private val _membersScores = _memberScoreIds.flatMapLatest { ids ->
        flow {
            val list = ids.map { id ->
                getMemberScoreById(id).first()
            }
            val usersWithScore = list.map { memberScore ->
                UserWithScore(
                    memberScore.id,
                    getUserById(memberScore.memberId).first(),
                    memberScore.score
                )
            }
            emit(usersWithScore)
        }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    init {
        viewModelScope.launch {
            _membersScores.collect { list ->
                membersScores.value = list
            }
        }
    }

    fun onDialogSavedChanges(userWithScore: UserWithScore) {
        membersScores.value = membersScores.value.map { us ->
            if (userWithScore.id == us.id) userWithScore else us
        }
    }

    fun onDialogDeleted(us: UserWithScore) {
        membersScores.value = membersScores.value.minus(us)
        println(membersScoresDeleted.size)
        membersScoresDeleted.add(us)
        println(membersScoresDeleted.size)
    }

    fun onAddButtonClicked() {
        val user = members.value.first()
        val id = System.currentTimeMillis().toString()
        membersScores.value = membersScores.value.plus(UserWithScore(id, user))
    }

    fun onCompleteClicked() {
        viewModelScope.launch {
            val result = Set(
                setId,
                _setTitle.value,
                membersScores.value.map { x -> MemberScore(x.id, x.user.id, x.score) }
            )
            membersScoresDeleted.forEach { x -> println(x.id) }
            updateSet(
                result,
                membersScoresDeleted.map { x -> MemberScore(x.id, x.user.id, x.score) },
                eventId
            )
            setUpdatedOrDeleted.value = true
        }
    }

    fun onDeleteClicked() {
        viewModelScope.launch {
            val result = Set(
                setId,
                _setTitle.value,
                membersScores.value.map { x -> MemberScore(x.id, x.user.id, x.score) }
            )
            deleteSet(result, eventId, roundId)
            setUpdatedOrDeleted.value = true
        }
    }

    fun onTitleChanged(title: String) {
        if (title == _setTitle.value) return
        viewModelScope.launch {
            _setTitle.value = title
        }
    }
}