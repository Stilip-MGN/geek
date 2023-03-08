package studio.stilip.geek.domain.usecase.authorization

import studio.stilip.geek.domain.repository_interface.UserRepository
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke() =
        repository.getCurrentUser()
}