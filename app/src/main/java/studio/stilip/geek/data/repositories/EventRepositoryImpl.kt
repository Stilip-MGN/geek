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
import studio.stilip.geek.domain.entities.Set
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

    override fun getMembersByEventId(id: String): Flow<List<String>> = callbackFlow {
        val members = database.child("Events").child(id).child("Members")
        val listener = members.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                launch {
                    send(snapshot.children.mapNotNull { it.getValue(User::class.java)!!.id })
                }
            }

            override fun onCancelled(error: DatabaseError) {
                cancel("Unable take events", error.toException())
            }

        })
        awaitClose { members.removeEventListener(listener) }
    }

    override suspend fun updateEvent(event: Event, deleteMembersId: List<String>) {
        val ref = database.child("Events").child(event.id)
        ref.updateChildren(
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
        deleteMembersId.forEach { id ->
            ref.child("Members").child(id).removeValue().await()
        }
    }

    override suspend fun createEvent(event: Event) {
        val ref = database.child("Events").push()
        ref.setValue(event.copy(id = ref.key!!)).await()
    }

    override suspend fun deleteEvent(id: String) {
        database.child("Chat").child(id).removeValue()
        database.child("Events").child(id).removeValue().await()
    }

    override suspend fun subscribeToEvent(userId: String, eventId: String) {
        database
            .child("Events")
            .child(eventId)
            .child("Members")
            .child(userId).updateChildren(
                mapOf("id" to userId)
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

    override suspend fun replaceMemberInScore(
        eventId: String,
        roundId: String,
        scoreId: String,
        userId: String
    ) {
        database
            .child("Events")
            .child(eventId)
            .child("Rounds")
            .child(roundId)
            .child("Scores")
            .child(scoreId).updateChildren(
                mapOf("memberId" to userId)
            ).await()
    }

    override suspend fun replaceScoreInScore(
        eventId: String,
        roundId: String,
        scoreId: String,
        score: Int
    ) {
        database
            .child("Events")
            .child(eventId)
            .child("Rounds")
            .child(roundId)
            .child("Scores")
            .child(scoreId).updateChildren(
                mapOf("score" to score)
            ).await()
    }

    override fun getRoundsByEventId(eventId: String): Flow<List<Round>> =
        callbackFlow {
            val rounds = database.child("Events").child(eventId).child("Rounds")
            val listener = rounds.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    launch {
                        send(snapshot.children.mapNotNull {
                            val r = it.getValue(Round::class.java)

                            val s = snapshot.child(r!!.id)
                                .child("Scores").children.mapNotNull { ds ->
                                    ds.getValue(Score::class.java)
                                }
                            r.copy(scores = s)
                        })
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    cancel("Unable take rounds", error.toException())
                }

            })
            awaitClose { rounds.removeEventListener(listener) }
        }

    override suspend fun createScoreByRoundId(eventId: String, roundId: String) {
        val ref = database
            .child("Events")
            .child(eventId)
            .child("Rounds")
            .child(roundId)
            .child("Scores")
            .push()
        ref.setValue(Score(id = ref.key!!)).await()
    }

    override suspend fun createRound(eventId: String, title: String) {
        val ref = database
            .child("Events")
            .child(eventId)
            .child("Rounds")
            .push()
        ref.setValue(Round(id = ref.key!!, title = title)).await()
    }

    override suspend fun deleteRound(eventId: String, roundId: String) {
        database
            .child("Events")
            .child(eventId)
            .child("Rounds")
            .child(roundId)
            .removeValue()
            .await()
    }

    override fun getSetById(id: String): Flow<Set> = callbackFlow {
        val set = database.child("Sets").child(id)
        val listener = set.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                launch {
                    snapshot.getValue(Set::class.java)?.let { send(it) }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                cancel("Unable take set", error.toException())
            }

        })
        awaitClose { set.removeEventListener(listener) }
    }

    override fun getMembersScoresBySetId(id: String): Flow<List<String>> = callbackFlow {
        val membersScores = database.child("Sets").child(id).child("MemberScore")
        val listener = membersScores.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                launch {
                    send(snapshot.children.mapNotNull { it.key })
                }
            }

            override fun onCancelled(error: DatabaseError) {
                cancel("Unable take membersScores", error.toException())
            }

        })
        awaitClose { membersScores.removeEventListener(listener) }
    }

    override fun getMemberScoreById(id: String): Flow<MemberScore> = callbackFlow {
        val memberScore = database.child("MemberScore").child(id)
        val listener = memberScore.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                launch {
                    snapshot.getValue(MemberScore::class.java)?.let { send(it) }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                cancel("Unable take MemberScore", error.toException())
            }

        })
        awaitClose { memberScore.removeEventListener(listener) }
    }

    override suspend fun updateSet(set: Set) {
        val refSet = database.child("Sets").child(set.id)
        refSet.updateChildren(
            mapOf(
                "title" to set.title,
            )
        )
        val refMS = database.child("MemberScore")
        val membersScores = database.child("Sets")
            .child(set.id)
            .child("MemberScore")

        membersScores.removeValue()
        set.membersScores.forEach { ms ->
            refMS.child(ms.id).setValue(ms)
            membersScores.child(ms.id).updateChildren(
                mapOf("id" to ms.id)
            )
        }
    }

    override fun getRoundsByEventId2(eventId: String): Flow<List<RoundNew>> = callbackFlow {
        val rounds = database.child("RoundsInfo").child(eventId)
        val listener = rounds.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                launch {
                    send(snapshot.children.mapNotNull {
                        val round = it.getValue(RoundNew::class.java)

                        val setsIds = snapshot.child(round!!.id)
                            .child("Sets").children.mapNotNull { ds ->
                                ds.key
                            }
                        round.copy(setsIds = setsIds)
                    })
                }
            }

            override fun onCancelled(error: DatabaseError) {
                cancel("Unable take rounds", error.toException())
            }

        })
        awaitClose { rounds.removeEventListener(listener) }
    }
}