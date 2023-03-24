package studio.stilip.geek.domain.usecase.game

import studio.stilip.geek.domain.repository_interface.GameRepository
import javax.inject.Inject

class RemoveGameFromWishlistByIdUseCase @Inject constructor(
    private val repository: GameRepository
) {
    suspend operator fun invoke(userId: String, gameId: String) =
        repository.removeGameFromWishlistById(userId, gameId)
}