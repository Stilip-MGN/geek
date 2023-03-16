package studio.stilip.geek.domain.repository_interface

import kotlinx.coroutines.flow.Flow
import studio.stilip.geek.domain.entities.Event

interface EventRepository {

    fun getAllEvents(): Flow<List<Event>>

    fun getEventById(id: String): Flow<Event>

    fun getMembersByEventId(id: String): Flow<List<String>>

    suspend fun updateEvent(event: Event)

    suspend fun createEvent(event: Event)

    suspend fun deleteEvent(id: String)

    suspend fun subscribeToEvent(userId: String, eventId: String)

    suspend fun unsubscribeFromEvent(userId: String, eventId: String)
}