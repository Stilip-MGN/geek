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
    stateHandle: SavedStateHandle
) : ViewModel() {
    private val _membersId = getMembersByEventId(stateHandle[EVENT_ID]!!)
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val userId = UserCacheManager.getUserId()
    val eventId: String = stateHandle[EVENT_ID]!!

    val event = getEvent(eventId)
        .stateIn(viewModelScope, SharingStarted.Eagerly, Event())

    val game = event.flatMapLatest { ev ->
        getGameById(ev.gameId)
    }.stateIn(viewModelScope, SharingStarted.Eagerly, Game())

    val user = getUserById(userId)
        .stateIn(viewModelScope, SharingStarted.Eagerly, User())

    val members = _membersId.flatMapLatest { ids ->
        flow { emit(ids.map { id -> getUserById(id).first() }) }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val rounds = getRoundsByEventId(eventId)
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

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
}