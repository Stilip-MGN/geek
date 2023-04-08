package studio.stilip.geek.app.events

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import studio.stilip.geek.data.UserCacheManager
import studio.stilip.geek.domain.entities.Event
import studio.stilip.geek.domain.usecase.event.GetAllEventsUseCase
import javax.inject.Inject

@HiltViewModel
class EventsViewModel @Inject constructor(
    private val getAllEvents: GetAllEventsUseCase,
) : ViewModel() {

    private val _checkedItems = MutableStateFlow(booleanArrayOf(false, false, false, false, false))
    private val _events = getAllEvents()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    private val _eventsFilter = MutableStateFlow<List<Event>>(emptyList())
    private val _searchQuery = MutableStateFlow("")

    init {
        viewModelScope.launch {
            _events.collect {
                filterEvents()
            }
        }
    }

    val userId = UserCacheManager.getUserId()
    val checkedItems: StateFlow<BooleanArray> = _checkedItems
    val events: StateFlow<List<Event>> = _eventsFilter

    fun onCheckSaved(list: BooleanArray) {
        viewModelScope.launch {
            _checkedItems.value = list
            filterEvents()
        }
    }

    fun onQuerySubmitChanged(query: String) {
        viewModelScope.launch {
            _searchQuery.value = query
            filterEvents()
        }
    }

    private fun filterEvents() {
        viewModelScope.launch {
            val checkedItems = _checkedItems.value
            val evs = _events.value
            val query = _searchQuery.value
            val result: MutableSet<Event> = mutableSetOf()
            if (checkedItems.all { isChecked -> !isChecked }) {
                if (query.isNotEmpty()) {
                    result.addAll(evs.filter { e ->
                        e.eventName.contains(query, true) || e.gameName.contains(query, true)
                    })
                } else result.addAll(evs)
            } else {
                if (checkedItems[0]) {
                    val eventsWithCategory = evs.filter { event -> event.ownId == userId }
                    if (query.isNotEmpty()) {
                        result.addAll(eventsWithCategory.filter { e ->
                            e.eventName.contains(query, true) || e.gameName.contains(query, true)
                        })
                    } else result.addAll(eventsWithCategory)
                }
                //TODO фильтрация по остальным
            }
            _eventsFilter.value = result.toList()
        }
    }
}