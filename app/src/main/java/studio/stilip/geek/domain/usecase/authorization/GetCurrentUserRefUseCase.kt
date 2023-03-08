package studio.stilip.geek.domain.usecase.authorization

import studio.stilip.geek.domain.repository_interface.UserRepository
import javax.inject.Inject

class GetCurrentUserRefUseCase @Inject constructor(
    private val repository: UserRepository
) {
    operator fun invoke() =
        repository.getCurrentUserRef()
}