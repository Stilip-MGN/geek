package studio.stilip.geek.domain.usecase.user

import studio.stilip.geek.domain.repository_interface.UserRepository
import javax.inject.Inject

class GetUserByIdUseCase @Inject constructor(
    private val repository: UserRepository
) {
    operator fun invoke(id: String) =
        repository.getUserById(id)
}