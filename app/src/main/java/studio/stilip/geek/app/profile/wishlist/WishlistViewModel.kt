package studio.stilip.geek.app.profile.wishlist

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
import studio.stilip.geek.domain.usecase.user.GetUserWishlistByIdUseCase
import javax.inject.Inject

@HiltViewModel
class WishlistViewModel @Inject constructor(
    private val currentUser: GetCurrentUserRefUseCase,
    private val getWishlistById: GetUserWishlistByIdUseCase,
) : ViewModel() {

    private val _wishlist = MutableStateFlow<List<Game>>(emptyList())
    private val userRef = currentUser()
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val wishlist: StateFlow<List<Game>> = _wishlist

    init {
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