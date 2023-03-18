package studio.stilip.geek.app.events.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import studio.stilip.geek.R
import studio.stilip.geek.app.events.event.EventFragment.Companion.EVENT_ID
import studio.stilip.geek.domain.entities.Event
import studio.stilip.geek.domain.entities.Game
import studio.stilip.geek.domain.entities.User
import studio.stilip.geek.domain.usecase.event.DeleteEventUseCase
import studio.stilip.geek.domain.usecase.event.GetEventByIdUseCase
import studio.stilip.geek.domain.usecase.event.GetMembersByEventIdUseCase
import studio.stilip.geek.domain.usecase.event.UpdateEventUseCase
import studio.stilip.geek.domain.usecase.game.GetAllGamesUserUseCase
import studio.stilip.geek.domain.usecase.user.GetUserByIdUseCase
import javax.inject.Inject

@HiltViewModel
class EventEditViewModel @Inject constructor(
    private val getEvent: GetEventByIdUseCase,
    private val getMembersByEventId: GetMembersByEventIdUseCase,
    private val getAllGames: GetAllGamesUserUseCase,
    private val getUserById: GetUserByIdUseCase,
    private val updateEvent: UpdateEventUseCase,
    private val deleteEvent: DeleteEventUseCase,
    stateHandle: SavedStateHandle
) : ViewModel() {

    private val _eventName = MutableStateFlow("")
    private val _game = MutableStateFlow(Game())
    private val _place = MutableStateFlow("")
    private val _date = MutableStateFlow("")
    private val _description = MutableStateFlow("")
    private val _maxMembers = MutableStateFlow("")

    val eventUpdated = MutableStateFlow<Boolean?>(null)
    val eventId: String = stateHandle[EVENT_ID]!!
    val eventNameHelper = MutableStateFlow(R.string.error_input_empty)

    val event = getEvent(eventId)
        .stateIn(viewModelScope, SharingStarted.Eagerly, Event())

    private val _membersId = getMembersByEventId(eventId)
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    private val _membersForDelete: MutableStateFlow<List<String>> =
        MutableStateFlow(arrayListOf())

    val members: StateFlow<List<User>> = _membersId
        .combine(_membersForDelete) { allIds, delIds ->
            allIds
                .filterNot { id -> delIds.contains(id) }
                .map { id -> getUserById(id).first() }
        }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

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

            updateEvent(
                Event(
                    id = eventId,
                    eventName = _eventName.value,
                    gameId = _game.value.id,
                    gameName = _game.value.name,
                    place = _place.value,
                    date = _date.value,
                    description = _description.value,
                    maxMembers = if (maxMembers.isEmpty()) 1 else maxMembers.toInt()
                ),
                _membersForDelete.value
            )
            eventUpdated.value = true
        }
    }

    fun delete() {
        viewModelScope.launch {
            deleteEvent(eventId)
        }
    }

    fun onDeleteMemberClicked(id: String) {
        viewModelScope.launch {
            _membersForDelete.value = _membersForDelete.value.plus(id)
        }
    }
}