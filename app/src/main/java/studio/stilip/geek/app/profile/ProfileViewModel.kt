package studio.stilip.geek.app.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import studio.stilip.geek.domain.entities.Game
import studio.stilip.geek.domain.entities.User
import studio.stilip.geek.domain.usecase.authorization.GetCurrentUserRefUseCase
import studio.stilip.geek.domain.usecase.user.GetUserByIdUseCase
import studio.stilip.geek.domain.usecase.user.GetUserCollectionByIdUseCase
import studio.stilip.geek.domain.usecase.user.GetUserWishlistByIdUseCase
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val currentUser: GetCurrentUserRefUseCase,
    private val getUserById: GetUserByIdUseCase,
    private val getCollectionById: GetUserCollectionByIdUseCase,
    private val getWishlistById: GetUserWishlistByIdUseCase,
) : ViewModel() {

    private val _user = MutableStateFlow(User())
    private val _collection = MutableStateFlow<List<Game>>(emptyList())
    private val _wishlist = MutableStateFlow<List<Game>>(emptyList())
    private val userRef = currentUser()
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val user: StateFlow<User> = _user
    val collection: StateFlow<List<Game>> = _collection
    val wishlist: StateFlow<List<Game>> = _wishlist

    init {
        viewModelScope.launch {
            userRef.collect {
                it?.let {
                    getUserById(it.uid).collect { u ->
                        _user.value = u
                    }
                }
            }
        }

        viewModelScope.launch {
            userRef.collect {
                it?.let {
                    getCollectionById(it.uid).collect { g ->
                        _collection.value = g
                    }
                }
            }
        }

        viewModelScope.launch {
            userRef.collect {
                it?.let {
                    getWishlistById(it.uid).collect { g ->
                        _wishlist.value = g
                    }
                }
            }
        }
    }
}