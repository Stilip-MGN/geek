package studio.stilip.geek.app.events

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import studio.stilip.geek.data.UserCacheManager
import studio.stilip.geek.domain.usecase.event.GetAllEventsUseCase
import javax.inject.Inject

@HiltViewModel
class EventsViewModel @Inject constructor(
    private val getAllEvents: GetAllEventsUseCase,
) : ViewModel() {

    val userId = UserCacheManager.getUserId()
    val events = getAllEvents()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
}