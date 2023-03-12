package studio.stilip.geek.app.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import studio.stilip.geek.data.UserCacheManager
import studio.stilip.geek.domain.entities.User
import studio.stilip.geek.domain.usecase.user.GetUserByIdUseCase
import studio.stilip.geek.domain.usecase.user.GetUserCollectionByIdUseCase
import studio.stilip.geek.domain.usecase.user.GetUserWishlistByIdUseCase
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUserById: GetUserByIdUseCase,
    private val getCollectionById: GetUserCollectionByIdUseCase,
    private val getWishlistById: GetUserWishlistByIdUseCase,
) : ViewModel() {

    private val userId = UserCacheManager.getUserId()

    val user = getUserById(userId)
        .stateIn(viewModelScope, SharingStarted.Eagerly, User())
    val collection = getCollectionById(userId)
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    val wishlist = getWishlistById(userId)
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
}