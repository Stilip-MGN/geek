package studio.stilip.geek.app.profile.wishlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import studio.stilip.geek.data.UserCacheManager
import studio.stilip.geek.domain.usecase.game.GetGameByIdUseCase
import studio.stilip.geek.domain.usecase.game.GetUserWishlistByIdUseCase
import studio.stilip.geek.domain.usecase.game.RemoveGameFromWishlistByIdUseCase
import javax.inject.Inject

@HiltViewModel
class WishlistViewModel @Inject constructor(
    private val getWishlistById: GetUserWishlistByIdUseCase,
    private val getGameById: GetGameByIdUseCase,
    private val removeGameFromWishlistById: RemoveGameFromWishlistByIdUseCase,
) : ViewModel() {

    private val userId = UserCacheManager.getUserId()
    private val wishlistGamesIds = getWishlistById(userId)
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val wishlist = wishlistGamesIds.map { ids ->
        ids.map { id ->
            getGameById(id).first()
        }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    fun onCloseClicked(id: String) {
        viewModelScope.launch {
            removeGameFromWishlistById(userId, id)
        }
    }
}