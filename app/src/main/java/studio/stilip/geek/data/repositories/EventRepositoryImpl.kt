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

    override suspend fun updateEvent(event: Event) {
        database.child("Events").child(event.id).updateChildren(
            mapOf(
                "eventName" to event.eventName,
                "gameId" to event.gameId,
                "gameName" to event.gameName,
                "maxMembers" to event.maxMembers,
                "date" to event.date,
                "description" to event.description,
                "place" to event.place,
            )
        ).await()
    }

    override suspend fun createEvent(event: Event) {
        val ref = database.child("Events").push()
        ref.setValue(event.copy(id = ref.key!!)).await()
    }

    override suspend fun deleteEvent(id: String) {
        database.child("Events").child(id).removeValue()
    }

    override suspend fun subscribeToEvent(user: User, eventId: String) {
        database
            .child("Events")
            .child(eventId)
            .child("Members")
            .child(user.id).updateChildren(
                mapOf(
                    "id" to user.id,
                    "name" to user.name,
                    "avatar" to user.avatar,
                )
            ).await()
    }

    override suspend fun unsubscribeFromEvent(userId: String, eventId: String) {
        database
            .child("Events")
            .child(eventId)
            .child("Members")
            .child(userId)
            .removeValue()
            .await()
    }
}