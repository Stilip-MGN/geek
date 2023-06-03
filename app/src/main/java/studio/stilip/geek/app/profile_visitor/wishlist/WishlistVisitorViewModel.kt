package studio.stilip.geek.app.profile_visitor.wishlist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import studio.stilip.geek.app.profile_visitor.ProfileVisitorFragment.Companion.VISITOR_ID
import studio.stilip.geek.domain.usecase.game.GetGameByIdUseCase
import studio.stilip.geek.domain.usecase.game.GetUserWishlistByIdUseCase
import javax.inject.Inject

@HiltViewModel
class WishlistVisitorViewModel @Inject constructor(
    private val getWishlistById: GetUserWishlistByIdUseCase,
    private val getGameById: GetGameByIdUseCase,
    stateHandle: SavedStateHandle
) : ViewModel() {

    private val userId: String = stateHandle[VISITOR_ID]!!
    private val wishlistGamesIds = getWishlistById(userId)
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val wishlist = wishlistGamesIds.map { ids ->
        ids.map { id ->
            getGameById(id).first()
        }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
}
