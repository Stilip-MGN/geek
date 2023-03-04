package studio.stilip.geek.data.repositories

import studio.stilip.geek.domain.entities.Game
import studio.stilip.geek.domain.repository_interface.GameRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GameRepositoryImpl @Inject constructor(
) : GameRepository {

    val games = arrayListOf(
        Game(name = "A", time = "30 min", countPlayers = "2-3", age = "10+"),
        Game(
            name = "B", time = "120 min", countPlayers = "1-3", age = "15+",
            description = "Вышел в поле дед",
            logo = "https://tesera.ru/images/items/1401961,3/200x200xpa/photo1.png"
        ),
    )

    override fun getAllGames(): List<Game> {
        return games
    }

}