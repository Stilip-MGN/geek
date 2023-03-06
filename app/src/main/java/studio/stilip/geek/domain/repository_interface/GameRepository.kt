package studio.stilip.geek.domain.repository_interface

import kotlinx.coroutines.flow.Flow
import studio.stilip.geek.domain.entities.Game

interface GameRepository {

    fun getAllGames(): Flow<List<Game>>

}