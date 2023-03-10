package studio.stilip.geek.domain.repository_interface

import kotlinx.coroutines.flow.Flow
import studio.stilip.geek.domain.entities.Game

interface GameRepository {

    fun getAllGames(): Flow<List<Game>>

    fun getGameById(id: String): Flow<Game>

    fun getUserCollectionGamesById(id: String): Flow<List<Game>>

    fun getUserWishlistGamesById(id: String): Flow<List<Game>>
}