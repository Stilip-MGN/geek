package studio.stilip.geek.domain.repository_interface

import studio.stilip.geek.domain.entities.Game

interface GameRepository {

    fun getAllGames(): List<Game>

}