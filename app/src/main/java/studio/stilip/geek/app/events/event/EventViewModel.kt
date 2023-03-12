package studio.stilip.geek.app.events.event

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import studio.stilip.geek.domain.usecase.event.GetAllEventsUseCase
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(
    private val getAllEvents: GetAllEventsUseCase,
) : ViewModel() {

    val event = getAllEvents()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
}