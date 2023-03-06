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
import studio.stilip.geek.domain.entities.Game
import studio.stilip.geek.domain.repository_interface.GameRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GameRepositoryImpl @Inject constructor(
    private val database: DatabaseReference
) : GameRepository {

    val games1 = arrayListOf(
        Game(name = "A", time = "30 min", countPlayers = "2-3", age = "10+"),
        Game(
            name = "B", time = "120 min", countPlayers = "1-3", age = "15+",
            description = "Вышел в поле дед",
            logo = "https://tesera.ru/images/items/1401961,3/200x200xpa/photo1.png"
        ),
    )

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

}