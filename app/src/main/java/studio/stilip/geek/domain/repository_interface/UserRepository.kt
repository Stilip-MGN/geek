package studio.stilip.geek.domain.repository_interface

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import studio.stilip.geek.domain.entities.User

interface UserRepository {

    suspend fun signIn(email: String, password: String): AuthResult

    suspend fun signUp(email: String, password: String): AuthResult

    suspend fun getCurrentUser(): FirebaseUser?

    fun getCurrentUserRef(): Flow<FirebaseUser?>

    suspend fun updateUserInfo(user: User): Void

    fun getUserById(id: String): Flow<User>

    suspend fun updateUserProfile(user: User, avatar: ByteArray)
}