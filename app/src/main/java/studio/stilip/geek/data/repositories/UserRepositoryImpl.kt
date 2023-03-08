package studio.stilip.geek.data.repositories

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import studio.stilip.geek.domain.entities.Game
import studio.stilip.geek.domain.entities.User
import studio.stilip.geek.domain.repository_interface.UserRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val database: DatabaseReference
) : UserRepository {

    override suspend fun signIn(email: String, password: String): AuthResult {
        return FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).await()
    }

    override suspend fun signUp(email: String, password: String): AuthResult {
        return FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).await()
    }

    override suspend fun getCurrentUser(): FirebaseUser? {
        return FirebaseAuth.getInstance().currentUser
    }

    override fun getCurrentUserRef(): Flow<FirebaseUser?> {
        return callbackFlow {
            send(FirebaseAuth.getInstance().currentUser)
            awaitClose()
        }
    }

    override suspend fun updateUserInfo(user: User): Void {
        return database
            .child("Users")
            .child(user.id)
            .setValue(user)
            .await()
    }

    override fun getUserById(id: String): Flow<User> {
        return callbackFlow {
            val user = database.child("Users").child(id)
            val listener = user.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    launch {
                        snapshot.getValue(User::class.java)?.let { send(it) }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    cancel("Unable take user", error.toException())
                }

            })
            awaitClose { user.removeEventListener(listener) }
        }
    }

}