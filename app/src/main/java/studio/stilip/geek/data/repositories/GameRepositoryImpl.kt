package studio.stilip.geek.data.repositories

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
import studio.stilip.geek.domain.repository_interface.GameRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GameRepositoryImpl @Inject constructor(
    private val database: DatabaseReference
) : GameRepository {

    override fun getAllGames(): Flow<List<Game>> = callbackFlow {
        val games = database.child("Games")
        val listener = games.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                launch {
                    send(snapshot.children.mapNotNull { it.getValue(Game::class.java) })
                }
            }

            override fun onCancelled(error: DatabaseError) {
                cancel("Unable take games", error.toException())
            }

        })
        awaitClose { games.removeEventListener(listener) }
    }

    override fun getGameById(id: String): Flow<Game> = callbackFlow {
        val games = database.child("Games").child(id)
        val listener = games.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                launch {
                    snapshot.getValue(Game::class.java)?.let { send(it) }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                cancel("Unable take games", error.toException())
            }

        })
        awaitClose { games.removeEventListener(listener) }
    }

    override fun getUserCollectionGamesById(id: String): Flow<List<String>> = callbackFlow {
        val gamesId = database.child("Collection").child(id)
        val listener = gamesId.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                launch {
                    send(snapshot.children.mapNotNull { it.key })
                }
            }

            override fun onCancelled(error: DatabaseError) {
                cancel("Unable take collection", error.toException())
            }

        })
        awaitClose { gamesId.removeEventListener(listener) }
    }

    override fun getUserWishlistGamesById(id: String): Flow<List<String>> = callbackFlow {
        val games = database.child("Wishlist").child(id)
        val listener = games.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                launch {
                    send(snapshot.children.mapNotNull { it.key })
                }
            }

            override fun onCancelled(error: DatabaseError) {
                cancel("Unable take wishlist", error.toException())
            }

        })
        awaitClose { games.removeEventListener(listener) }
    }

    override suspend fun addGamesToCollectionByUserId(userId: String, games: List<String>) {
        val userCollection = database.child("Collection").child(userId)

        for (gameId in games) {
            userCollection.child(gameId).updateChildren(
                mapOf("id" to gameId)
            ).await()
        }
    }

    override suspend fun addGamesToWishlistByUserId(userId: String, games: List<String>) {
        val userWishlist = database.child("Wishlist").child(userId)

        for (gameId in games) {
            userWishlist.child(gameId).updateChildren(
                mapOf("id" to gameId)
            ).await()
        }
    }

}