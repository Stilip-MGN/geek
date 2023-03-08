package studio.stilip.geek.app.games.gameinfo

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import studio.stilip.geek.app.games.gameinfo.GameInfoFragment.Companion.GAME_ID
import studio.stilip.geek.domain.entities.Game
import studio.stilip.geek.domain.usecase.game.GetGameByIdUseCase
import javax.inject.Inject

@HiltViewModel
class GameInfoViewModel @Inject constructor(
    private val getGameById: GetGameByIdUseCase,
    stateHandle: SavedStateHandle
) : ViewModel() {

    val game: StateFlow<Game> = getGameById(stateHandle[GAME_ID]!!)
        .stateIn(viewModelScope, SharingStarted.Eagerly, Game())

}