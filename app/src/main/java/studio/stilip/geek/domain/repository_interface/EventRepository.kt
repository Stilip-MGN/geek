package studio.stilip.geek.domain.repository_interface

import kotlinx.coroutines.flow.Flow
import studio.stilip.geek.domain.entities.*
import studio.stilip.geek.domain.entities.Set

interface EventRepository {

    fun getAllEvents(): Flow<List<Event>>

    fun getEventById(id: String): Flow<Event>

    fun getMembersByEventId(id: String): Flow<List<String>>

    suspend fun updateEvent(event: Event, deleteMembersId: List<String>)

    suspend fun createEvent(event: Event)

    suspend fun deleteEvent(id: String)

    suspend fun subscribeToEvent(userId: String, eventId: String)

    suspend fun unsubscribeFromEvent(userId: String, eventId: String)

    fun getSetById(id: String): Flow<Set>

    fun getMembersScoresBySetId(id: String): Flow<List<String>>

    fun getMemberScoreById(id: String): Flow<MemberScore>

    suspend fun updateSet(set: Set, membersScoresDeleted: List<MemberScore>, eventId: String)

    fun getRoundsByEventId(eventId: String): Flow<List<Round>>

    suspend fun createSet(
        title: String,
        membersScores: List<MemberScore>,
        eventId: String,
        roundId: String
    )

    suspend fun createRoundNew(title: String, eventId: String)

    suspend fun deleteSet(set: Set, eventId: String, roundId: String)
}