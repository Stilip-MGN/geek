package studio.stilip.geek.app.events.event

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import studio.stilip.geek.app.events.event.EventFragment.Companion.EVENT_ID
import studio.stilip.geek.data.UserCacheManager
import studio.stilip.geek.domain.entities.Event
import studio.stilip.geek.domain.entities.Game
import studio.stilip.geek.domain.entities.User
import studio.stilip.geek.domain.usecase.event.GetEventByIdUseCase
import studio.stilip.geek.domain.usecase.event.GetMembersByEventIdUseCase
import studio.stilip.geek.domain.usecase.event.SubscribeToEventUseCase
import studio.stilip.geek.domain.usecase.event.UnsubscribeFromEventUseCase
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
    stateHandle: SavedStateHandle
) : ViewModel() {

    private val _game = MutableStateFlow(Game())
    val userId = UserCacheManager.getUserId()
    val eventId: String = stateHandle[EVENT_ID]!!

    val game: StateFlow<Game> = _game
    val event = getEvent(eventId)
        .stateIn(viewModelScope, SharingStarted.Eagerly, Event())
    val members = getMembersByEventId(stateHandle[EVENT_ID]!!)
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    val user = getUserById(userId)
        .stateIn(viewModelScope, SharingStarted.Eagerly, User())

    init {

        viewModelScope.launch {
            event.collectLatest { ev ->
                getGameById(ev.gameId).collect { g ->
                    _game.value = g
                }
            }
        }
    }

    fun onSubscribeClick() {
        viewModelScope.launch {
            subscribeToEvent(user.value, eventId)
        }
    }

    fun onUnsubscribeClick() {
        viewModelScope.launch {
            unsubscribeFromEvent(userId, eventId)
        }
    }

}