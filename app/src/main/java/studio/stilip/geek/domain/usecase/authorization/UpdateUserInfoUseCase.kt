package studio.stilip.geek.domain.usecase.authorization

import studio.stilip.geek.domain.entities.User
import studio.stilip.geek.domain.repository_interface.UserRepository
import javax.inject.Inject

class UpdateUserInfoUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(user: User) =
        repository.updateUserInfo(user)
}