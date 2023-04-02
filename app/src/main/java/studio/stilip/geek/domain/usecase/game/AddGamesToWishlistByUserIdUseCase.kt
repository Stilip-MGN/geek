package studio.stilip.geek.domain.usecase.game

import studio.stilip.geek.domain.repository_interface.GameRepository
import javax.inject.Inject

class AddGamesToWishlistByUserIdUseCase @Inject constructor(
    private val repository: GameRepository
) {
    suspend operator fun invoke(userId: String, games: List<String>) =
        repository.addGamesToWishlistByUserId(userId, games)
}