package studio.stilip.geek.app.profile.collection.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import studio.stilip.geek.data.UserCacheManager
import studio.stilip.geek.domain.usecase.game.AddGamesToCollectionByUserIdUseCase
import studio.stilip.geek.domain.usecase.game.GetAllGamesUserUseCase
import studio.stilip.geek.domain.usecase.game.GetUserCollectionByIdUseCase
import javax.inject.Inject

@HiltViewModel
class CollectionAddViewModel @Inject constructor(
    private val getCollectionById: GetUserCollectionByIdUseCase,
    private val getAllGamesUserUseCase: GetAllGamesUserUseCase,
    private val addGamesToCollectionByUserId: AddGamesToCollectionByUserIdUseCase,
) : ViewModel() {

    private val userId = UserCacheManager.getUserId()
    private val gamesForAdd: MutableStateFlow<List<String>> =
        MutableStateFlow(emptyList())
    private val collectionGamesIds = getCollectionById(userId)
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    private val allGames = getAllGamesUserUseCase()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val gamesAdded: MutableStateFlow<Boolean?> = MutableStateFlow(null)

    val games = allGames.combine(collectionGamesIds) { games, collection ->
        games to collection
    }.map { (games, collection) ->
        games.filterNot { game ->
            collection.contains(game.id)
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
            addGamesToCollectionByUserId(userId, gamesForAdd.value)
            gamesAdded.value = true
        }
    }
}