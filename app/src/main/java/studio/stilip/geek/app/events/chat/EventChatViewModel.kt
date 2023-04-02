package studio.stilip.geek.app.events.chat

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import studio.stilip.geek.app.events.event.EventFragment
import studio.stilip.geek.data.UserCacheManager
import studio.stilip.geek.domain.usecase.event.GetAllEventsUseCase
import javax.inject.Inject

@HiltViewModel
class EventChatViewModel @Inject constructor(
    stateHandle: SavedStateHandle
) : ViewModel() {

    val userId = UserCacheManager.getUserId()
    val eventId: String = stateHandle[EventFragment.EVENT_ID]!!
}