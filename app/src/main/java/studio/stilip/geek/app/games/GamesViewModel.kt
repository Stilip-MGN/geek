package studio.stilip.geek.app.games

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import studio.stilip.geek.domain.usecase.authorization.GetCurrentUserRefUseCase
import studio.stilip.geek.domain.usecase.game.GetAllGamesUserUseCase
import javax.inject.Inject

@HiltViewModel
class GamesViewModel @Inject constructor(
    private val getAllGames: GetAllGamesUserUseCase,
    private val currentUserRef: GetCurrentUserRefUseCase,
) : ViewModel() {

    val games = getAllGames()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val user = currentUserRef()
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

}