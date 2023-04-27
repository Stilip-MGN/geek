package studio.stilip.geek.app.profile_visitor

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import studio.stilip.geek.app.profile_visitor.ProfileVisitorFragment.Companion.VISITOR_ID
import studio.stilip.geek.domain.entities.User
import studio.stilip.geek.domain.usecase.game.GetGameByIdUseCase
import studio.stilip.geek.domain.usecase.user.GetUserByIdUseCase
import studio.stilip.geek.domain.usecase.game.GetUserCollectionByIdUseCase
import studio.stilip.geek.domain.usecase.game.GetUserWishlistByIdUseCase
import javax.inject.Inject

@HiltViewModel
class ProfileVisitorViewModel @Inject constructor(
    private val getUserById: GetUserByIdUseCase,
    private val getCollectionById: GetUserCollectionByIdUseCase,
    private val getWishlistById: GetUserWishlistByIdUseCase,
    private val getGameById: GetGameByIdUseCase,
    stateHandle: SavedStateHandle
) : ViewModel() {

    private val visitorId: String = stateHandle[VISITOR_ID]!!
    private val collectionGamesIds = getCollectionById(visitorId)
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    private val wishlistGamesIds = getWishlistById(visitorId)
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val user = getUserById(visitorId)
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