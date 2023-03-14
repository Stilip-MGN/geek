package studio.stilip.geek.app.events.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import studio.stilip.geek.app.events.event.EventFragment.Companion.EVENT_ID
import studio.stilip.geek.domain.entities.Event
import studio.stilip.geek.domain.usecase.event.GetEventByIdUseCase
import studio.stilip.geek.domain.usecase.event.GetMembersByEventIdUseCase
import studio.stilip.geek.domain.usecase.event.UpdateEventUseCase
import studio.stilip.geek.domain.usecase.game.GetAllGamesUserUseCase
import javax.inject.Inject

@HiltViewModel
class EventEditViewModel @Inject constructor(
    private val getEvent: GetEventByIdUseCase,
    private val getMembersByEventId: GetMembersByEventIdUseCase,
    private val getAllGames: GetAllGamesUserUseCase,
    private val updateEvent: UpdateEventUseCase,
    stateHandle: SavedStateHandle
) : ViewModel() {

    val eventId: String = stateHandle[EVENT_ID]!!

    val event = getEvent(eventId)
        .stateIn(viewModelScope, SharingStarted.Eagerly, Event())
    val members = getMembersByEventId(eventId)
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    val games = getAllGames()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())


    fun update(event: Event) {
        viewModelScope.launch {
            updateEvent(event)
        }
    }
}