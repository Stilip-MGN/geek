package studio.stilip.geek.domain.repository_interface

import kotlinx.coroutines.flow.Flow
import studio.stilip.geek.domain.entities.Message

interface ChatRepository {

    fun getMessagesByEventId(eventId: String): Flow<List<Message>>

    suspend fun addMessageToEvent(eventId: String, message: Message)
}