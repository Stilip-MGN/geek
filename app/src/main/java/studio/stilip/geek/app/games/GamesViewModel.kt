package studio.stilip.geek.app.games

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import studio.stilip.geek.domain.usecase.game.GetAllGamesUserUseCase
import javax.inject.Inject

@HiltViewModel
class GamesViewModel @Inject constructor(
    private val getAllGames: GetAllGamesUserUseCase,
) : ViewModel() {

    val games = getAllGames()

}