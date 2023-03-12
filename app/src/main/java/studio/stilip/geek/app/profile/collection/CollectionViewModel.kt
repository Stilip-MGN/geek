package studio.stilip.geek.app.profile.collection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import studio.stilip.geek.domain.entities.Game
import studio.stilip.geek.domain.usecase.authorization.GetCurrentUserRefUseCase
import studio.stilip.geek.domain.usecase.user.GetUserCollectionByIdUseCase
import javax.inject.Inject

@HiltViewModel
class CollectionViewModel @Inject constructor(
    private val currentUser: GetCurrentUserRefUseCase,
    private val getCollectionById: GetUserCollectionByIdUseCase,
) : ViewModel() {

    private val _collection = MutableStateFlow<List<Game>>(emptyList())
    private val userRef = currentUser()
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val collection: StateFlow<List<Game>> = _collection

    init {
        viewModelScope.launch {
            userRef.collect {
                it?.let {
                    getCollectionById(it.uid).collect { g ->
                        _collection.value = g
                    }
                }
            }
        }
    }
}