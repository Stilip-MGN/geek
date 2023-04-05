package studio.stilip.geek.app.events.chat

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import studio.stilip.geek.app.events.event.EventFragment
import studio.stilip.geek.data.UserCacheManager
import studio.stilip.geek.domain.entities.Message
import studio.stilip.geek.domain.usecase.chat.AddMessageToEventUseCase
import studio.stilip.geek.domain.usecase.chat.GetMessagesByEventIdUseCase
import studio.stilip.geek.domain.usecase.user.GetUserByIdUseCase
import javax.inject.Inject

@HiltViewModel
class EventChatViewModel @Inject constructor(
    private val getMessagesByEventId: GetMessagesByEventIdUseCase,
    private val addMessageToEvent: AddMessageToEventUseCase,
    private val getUserById: GetUserByIdUseCase,
    stateHandle: SavedStateHandle
) : ViewModel() {

    private val _message = MutableStateFlow("")
    val userId = UserCacheManager.getUserId()
    val eventId: String = stateHandle[EventFragment.EVENT_ID]!!

    private val _messages = getMessagesByEventId(eventId)
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    //Замения id других пользователей на их актуальное имя (пользователя оставляем)
    val messages = _messages.flatMapLatest { messages ->
        flow {
            emit(messages.map { message ->
                if (message.createdBy != userId) {
                    val name = getUserById(message.createdBy).first().name
                    message.copy(createdBy = name)
                } else
                    message
            })
        }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    fun onSendClicked() {
        viewModelScope.launch {
            if (_message.value.isNotEmpty()) {
                val message = Message("", System.currentTimeMillis(), userId, _message.value)
                addMessageToEvent(eventId, message)
            }
        }
    }

    fun onMessageChanged(message: String) {
        if (message == _message.value) return
        viewModelScope.launch {
            _message.value = message
        }
    }
}