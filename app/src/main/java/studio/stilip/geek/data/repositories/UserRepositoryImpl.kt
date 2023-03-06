package studio.stilip.geek.data.repositories

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.tasks.await
import studio.stilip.geek.domain.repository_interface.UserRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val database: DatabaseReference
) : UserRepository {

    override suspend fun signIn(email:String, password:String): AuthResult {
        return FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).await()
    }

    override suspend fun signUp(email:String, password:String): AuthResult {
        return FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).await()
    }

}