package studio.stilip.geek.domain.usecase.game

import studio.stilip.geek.domain.repository_interface.GameRepository
import javax.inject.Inject

class GetUserCollectionByIdUseCase @Inject constructor(
    private val repository: GameRepository
) {
    operator fun invoke(id: String) = repository.getUserCollectionGamesById(id)
}