package studio.stilip.geek.domain.repository_interface

import kotlinx.coroutines.flow.Flow
import studio.stilip.geek.domain.entities.Game

interface GameRepository {

    fun getAllGames(): Flow<List<Game>>

    fun getGameById(id: String): Flow<Game>

    fun getUserCollectionGamesById(id: String): Flow<List<String>>

    fun getUserWishlistGamesById(id: String): Flow<List<String>>

    suspend fun addGamesToCollectionByUserId(userId: String, games: List<String>)

    suspend fun addGamesToWishlistByUserId(userId: String, games: List<String>)
}