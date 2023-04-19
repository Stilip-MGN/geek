package studio.stilip.geek.app.events.set.create

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import studio.stilip.geek.app.events.event.EventFragment.Companion.EVENT_ID
import studio.stilip.geek.app.events.event.EventFragment.Companion.ROUND_ID
import studio.stilip.geek.domain.entities.MemberScore
import studio.stilip.geek.domain.entities.UserWithScore
import studio.stilip.geek.domain.usecase.event.*
import studio.stilip.geek.domain.usecase.user.GetUserByIdUseCase
import javax.inject.Inject

@HiltViewModel
class SetCreateViewModel @Inject constructor(
    private val getMembersByEventId: GetMembersByEventIdUseCase,
    private val getUserById: GetUserByIdUseCase,
    private val createSet: CreateSetUseCase,
    stateHandle: SavedStateHandle
) : ViewModel() {

    val membersScores = MutableStateFlow(emptyList<UserWithScore>())
    val setCreated = MutableStateFlow<Boolean?>(null)
    private val _setTitle = MutableStateFlow("")

    val eventId: String = stateHandle[EVENT_ID]!!
    private val roundId: String = stateHandle[ROUND_ID]!!

    private val _membersId = getMembersByEventId(eventId)
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val members = _membersId.flatMapLatest { ids ->
        flow { emit(ids.map { id -> getUserById(id).first() }) }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    fun onDialogSavedChanges(userWithScore: UserWithScore) {
        membersScores.value = membersScores.value.map { us ->
            if (userWithScore.id == us.id) userWithScore else us
        }
    }

    fun onDialogDeleted(us: UserWithScore) {
        membersScores.value = membersScores.value.minus(us)
    }

    fun onAddButtonClicked() {
        val user = members.value.first()
        val id = System.currentTimeMillis().toString()
        membersScores.value = membersScores.value.plus(UserWithScore(id, user))
    }

    fun onCompleteClicked() {
        viewModelScope.launch {
            createSet(
                title = _setTitle.value,
                membersScores = membersScores.value.map { x ->
                    MemberScore(x.id, x.user.id, x.score)
                },
                eventId = eventId,
                roundId = roundId
            )
            setCreated.value = true
        }
    }

    fun onTitleChanged(title: String) {
        if (title == _setTitle.value) return
        viewModelScope.launch {
            _setTitle.value = title
        }
    }
}