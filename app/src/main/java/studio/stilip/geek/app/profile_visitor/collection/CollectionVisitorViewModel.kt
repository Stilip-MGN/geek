package studio.stilip.geek.app.profile_visitor.collection

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import studio.stilip.geek.app.profile_visitor.ProfileVisitorFragment.Companion.VISITOR_ID
import studio.stilip.geek.domain.usecase.game.GetGameByIdUseCase
import studio.stilip.geek.domain.usecase.game.GetUserCollectionByIdUseCase
import javax.inject.Inject

@HiltViewModel
class CollectionVisitorViewModel @Inject constructor(
    private val getCollectionById: GetUserCollectionByIdUseCase,
    private val getGameById: GetGameByIdUseCase,
    stateHandle: SavedStateHandle
) : ViewModel() {

    private val visitorId: String = stateHandle[VISITOR_ID]!!
    private val collectionGamesIds = getCollectionById(visitorId)
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val collection = collectionGamesIds.map { ids ->
        ids.map { id ->
            getGameById(id).first()
        }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
}