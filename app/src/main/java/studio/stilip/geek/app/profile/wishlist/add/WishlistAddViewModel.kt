package studio.stilip.geek.app.profile.wishlist.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import studio.stilip.geek.data.UserCacheManager
import studio.stilip.geek.domain.usecase.game.*
import javax.inject.Inject

@HiltViewModel
class WishlistAddViewModel @Inject constructor(
    private val getWishlistById: GetUserWishlistByIdUseCase,
    private val getAllGamesUserUseCase: GetAllGamesUserUseCase,
    private val addGamesToWishlistByUserId: AddGamesToWishlistByUserIdUseCase,
) : ViewModel() {

    private val userId = UserCacheManager.getUserId()
    private val gamesForAdd: MutableStateFlow<List<String>> =
        MutableStateFlow(emptyList())
    private val wishlistGamesIds = getWishlistById(userId)
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    private val allGames = getAllGamesUserUseCase()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val gamesAdded: MutableStateFlow<Boolean?> = MutableStateFlow(null)

    val games = allGames.combine(wishlistGamesIds) { games, wishlist ->
        games to wishlist
    }.map { (games, wishlist) ->
        games.filterNot { game ->
            wishlist.contains(game.id)
        }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    fun onGameClicked(id: String, isChecked: Boolean) {
        viewModelScope.launch {
            gamesForAdd.value = if (isChecked)
                gamesForAdd.value.plus(id)
            else
                gamesForAdd.value.minus(id)
            println(gamesForAdd.value.joinToString(", "))
        }
    }

    fun onCompleteClicked() {
        viewModelScope.launch {
            addGamesToWishlistByUserId(userId, gamesForAdd.value)
            gamesAdded.value = true
        }
    }
}