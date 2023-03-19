package studio.stilip.geek.domain.repository_interface

import kotlinx.coroutines.flow.Flow
import studio.stilip.geek.domain.entities.Event
import studio.stilip.geek.domain.entities.Round

interface EventRepository {

    fun getAllEvents(): Flow<List<Event>>

    fun getEventById(id: String): Flow<Event>

    fun getMembersByEventId(id: String): Flow<List<String>>

    suspend fun updateEvent(event: Event, deleteMembersId: List<String>)

    suspend fun createEvent(event: Event)

    suspend fun deleteEvent(id: String)

    suspend fun subscribeToEvent(userId: String, eventId: String)

    suspend fun unsubscribeFromEvent(userId: String, eventId: String)

    suspend fun replaceMemberInScore(
        eventId: String,
        roundId: String,
        scoreId: String,
        userId: String
    )

    suspend fun replaceScoreInScore(
        eventId: String,
        roundId: String,
        scoreId: String,
        score: Int
    )

    fun getRoundsByEventId(eventId: String): Flow<List<Round>>

    suspend fun createScoreByRoundId(eventId: String, roundId: String)

    suspend fun createRound(eventId: String, title: String)

    suspend fun deleteRound(eventId: String, roundId: String)
}