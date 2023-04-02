package studio.stilip.geek.app.profile.collection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import studio.stilip.geek.data.UserCacheManager
import studio.stilip.geek.domain.usecase.game.GetGameByIdUseCase
import studio.stilip.geek.domain.usecase.game.GetUserCollectionByIdUseCase
import studio.stilip.geek.domain.usecase.game.RemoveGameFromCollectionByIdUseCase
import javax.inject.Inject

@HiltViewModel
class CollectionViewModel @Inject constructor(
    private val getCollectionById: GetUserCollectionByIdUseCase,
    private val getGameById: GetGameByIdUseCase,
    private val removeGameFromCollectionById: RemoveGameFromCollectionByIdUseCase,
) : ViewModel() {

    private val userId = UserCacheManager.getUserId()
    private val collectionGamesIds = getCollectionById(userId)
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val collection = collectionGamesIds.map { ids ->
        ids.map { id ->
            getGameById(id).first()
        }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    fun onCloseClicked(id: String) {
        viewModelScope.launch {
            removeGameFromCollectionById(userId, id)
        }
    }
}