package studio.stilip.geek.app.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import studio.stilip.geek.data.UserCacheManager
import studio.stilip.geek.domain.entities.User
import studio.stilip.geek.domain.usecase.game.GetGameByIdUseCase
import studio.stilip.geek.domain.usecase.user.GetUserByIdUseCase
import studio.stilip.geek.domain.usecase.user.GetUserCollectionByIdUseCase
import studio.stilip.geek.domain.usecase.user.GetUserWishlistByIdUseCase
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUserById: GetUserByIdUseCase,
    private val getCollectionById: GetUserCollectionByIdUseCase,
    private val getWishlistById: GetUserWishlistByIdUseCase,
    private val getGameById: GetGameByIdUseCase,
) : ViewModel() {

    private val userId = UserCacheManager.getUserId()
    private val collectionGamesIds = getCollectionById(userId)
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    private val wishlistGamesIds = getWishlistById(userId)
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val user = getUserById(userId)
        .stateIn(viewModelScope, SharingStarted.Eagerly, User())

    val collection = collectionGamesIds.map { ids ->
        ids.map { id ->
            getGameById(id).first()
        }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val wishlist = wishlistGamesIds.map { ids ->
        ids.map { id ->
            getGameById(id).first()
        }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
}