package studio.stilip.geek.domain.usecase.authorization

import studio.stilip.geek.domain.repository_interface.UserRepository
import javax.inject.Inject

class SignInUserUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(email: String, password: String) =
        repository.signIn(email, password)
}