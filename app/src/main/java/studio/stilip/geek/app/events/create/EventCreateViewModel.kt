package studio.stilip.geek.app.events.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import studio.stilip.geek.R
import studio.stilip.geek.domain.entities.Event
import studio.stilip.geek.domain.entities.Game
import studio.stilip.geek.domain.usecase.event.CreateEventUseCase
import studio.stilip.geek.domain.usecase.game.GetAllGamesUserUseCase
import javax.inject.Inject

@HiltViewModel
class EventCreateViewModel @Inject constructor(
    private val getAllGames: GetAllGamesUserUseCase,
    private val createEvent: CreateEventUseCase
) : ViewModel() {

    private val _eventName = MutableStateFlow("")
    private val _game = MutableStateFlow(Game())
    private val _place = MutableStateFlow("")
    private val _date = MutableStateFlow("")
    private val _description = MutableStateFlow("")
    private val _maxMembers = MutableStateFlow("")

    val eventCreated = MutableStateFlow<Boolean?>(null)
    val eventNameHelper = MutableStateFlow(R.string.error_input_empty)
    val games = getAllGames()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    fun onEventNameChanged(name: String) {
        if (name == _eventName.value) return
        viewModelScope.launch {
            if (name.isEmpty())
                eventNameHelper.value = R.string.error_input_empty
            else
                eventNameHelper.value = R.string.empty_text
            _eventName.value = name
        }
    }

    fun onGameChanged(game: Game) {
        if (game == _game.value) return
        viewModelScope.launch {
            _game.value = game
        }
    }

    fun onPlaceChanged(place: String) {
        if (place == _place.value) return
        viewModelScope.launch {
            _place.value = place
        }
    }

    fun onDateChanged(date: String) {
        if (date == _date.value) return
        viewModelScope.launch {
            _date.value = date
        }
    }

    fun onDescriptionChanged(description: String) {
        if (description == _description.value) return
        viewModelScope.launch {
            _description.value = description
        }
    }

    fun onMaxMembersChanged(maxMembers: String) {
        if (maxMembers == _maxMembers.value) return
        viewModelScope.launch {
            _maxMembers.value = maxMembers
        }
    }

    fun onCompleteClicked() {
        if (_eventName.value.isEmpty()) return
        viewModelScope.launch {
            val maxMembers = _maxMembers.value
            val countMembers = if (maxMembers.isEmpty()) 1 else {
                if (maxMembers.toInt() < 100) maxMembers.toInt() else 100
            }

            createEvent(
                Event(
                    eventName = _eventName.value,
                    gameId = _game.value.id,
                    gameName = _game.value.name,
                    place = _place.value,
                    date = _date.value,
                    description = _description.value,
                    maxMembers = countMembers
                )
            )
            eventCreated.value = true
        }
    }
}