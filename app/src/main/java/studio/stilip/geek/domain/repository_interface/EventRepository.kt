package studio.stilip.geek.domain.repository_interface

import kotlinx.coroutines.flow.Flow
import studio.stilip.geek.domain.entities.Event
import studio.stilip.geek.domain.entities.User

interface EventRepository {

    fun getAllEvents(): Flow<List<Event>>

    fun getEventById(id: String): Flow<Event>

    fun getMembersByEventId(id: String): Flow<List<User>>

    suspend fun updateEvent(event: Event)
}