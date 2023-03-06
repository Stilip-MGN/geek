package studio.stilip.geek.domain.repository_interface

import com.google.firebase.auth.AuthResult

interface UserRepository {

    suspend fun signIn(email: String, password: String): AuthResult

    suspend fun signUp(email: String, password: String): AuthResult

}