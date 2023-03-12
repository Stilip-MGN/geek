package studio.stilip.geek.domain.usecase.user

import studio.stilip.geek.domain.repository_interface.GameRepository
import javax.inject.Inject

class GetUserWishlistByIdUseCase @Inject constructor(
    private val repository: GameRepository
) {
    operator fun invoke(id: String) = repository.getUserWishlistGamesById(id)
}