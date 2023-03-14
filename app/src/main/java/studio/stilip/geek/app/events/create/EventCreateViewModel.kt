package studio.stilip.geek.app.events.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import studio.stilip.geek.domain.entities.Event
import studio.stilip.geek.domain.usecase.event.CreateEventUseCase
import studio.stilip.geek.domain.usecase.game.GetAllGamesUserUseCase
import javax.inject.Inject

@HiltViewModel
class EventCreateViewModel @Inject constructor(
    private val getAllGames: GetAllGamesUserUseCase,
    private val createEvent: CreateEventUseCase
) : ViewModel() {
    val games = getAllGames()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())


    fun create(event: Event) {
        viewModelScope.launch {
            createEvent(event)
        }
    }
}