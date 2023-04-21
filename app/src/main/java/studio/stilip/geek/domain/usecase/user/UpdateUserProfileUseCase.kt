package studio.stilip.geek.domain.usecase.user

import studio.stilip.geek.domain.entities.User
import studio.stilip.geek.domain.repository_interface.UserRepository
import javax.inject.Inject

class UpdateUserProfileUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(user: User, avatar: ByteArray) =
        repository.updateUserProfile(user, avatar)
}