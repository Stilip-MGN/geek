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
import studio.stilip.geek.domain.entities.*
import studio.stilip.geek.domain.repository_interface.ChatRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepositoryImpl @Inject constructor(
    private val database: DatabaseReference
) : ChatRepository {
    override fun getMessagesByEventId(eventId: String): Flow<List<Message>> = callbackFlow {
        val chat = database.child("Chat").child(eventId)
        val listener = chat.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                launch {
                    send(snapshot.children.mapNotNull { it.getValue(Message::class.java) })
                }
            }

            override fun onCancelled(error: DatabaseError) {
                cancel("Unable take chat", error.toException())
            }

        })
        awaitClose { chat.removeEventListener(listener) }
    }

    override suspend fun addMessageToEvent(eventId: String, message: Message) {
        val ref = database.child("Chat").child(eventId).push()
        ref.setValue(message.copy(id = ref.key!!)).await()
    }


}