package studio.stilip.geek.domain.usecase.authorization

import studio.stilip.geek.domain.repository_interface.UserRepository
import javax.inject.Inject

class SignUpUserUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(email: String, password: String) =
        repository.signUp(email, password)
}