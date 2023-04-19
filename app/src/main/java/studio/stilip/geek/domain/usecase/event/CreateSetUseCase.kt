package studio.stilip.geek.domain.usecase.event

import studio.stilip.geek.domain.entities.MemberScore
import studio.stilip.geek.domain.repository_interface.EventRepository
import javax.inject.Inject

class CreateSetUseCase @Inject constructor(
    private val repository: EventRepository
) {
    suspend operator fun invoke(
        title: String,
        membersScores: List<MemberScore>,
        eventId: String,
        roundId: String
    ) = repository.createSet(title, membersScores, eventId, roundId)
}