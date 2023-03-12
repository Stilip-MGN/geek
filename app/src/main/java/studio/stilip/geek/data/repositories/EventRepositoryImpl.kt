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
import studio.stilip.geek.domain.entities.Event
import studio.stilip.geek.domain.entities.User
import studio.stilip.geek.domain.repository_interface.EventRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventRepositoryImpl @Inject constructor(
    private val database: DatabaseReference
) : EventRepository {

    override fun getAllEvents(): Flow<List<Event>> = callbackFlow {
        val events = database.child("Events")
        val listener = events.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                launch {
                    send(snapshot.children.mapNotNull { it.getValue(Event::class.java) })
                }
            }

            override fun onCancelled(error: DatabaseError) {
                cancel("Unable take events", error.toException())
            }

        })
        awaitClose { events.removeEventListener(listener) }
    }

    override fun getEventById(id: String): Flow<Event> = callbackFlow {
        val event = database.child("Events").child(id)
        val listener = event.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                launch {
                    snapshot.getValue(Event::class.java)?.let { send(it) }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                cancel("Unable take event", error.toException())
            }

        })
        awaitClose { event.removeEventListener(listener) }
    }

    override fun getMembersByEventId(id: String): Flow<List<User>> = callbackFlow {
        val members = database.child("Events").child(id).child("Members")
        val listener = members.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                launch {
                    send(snapshot.children.mapNotNull { it.getValue(User::class.java) })
                }
            }

            override fun onCancelled(error: DatabaseError) {
                cancel("Unable take events", error.toException())
            }

        })
        awaitClose { members.removeEventListener(listener) }
    }
}