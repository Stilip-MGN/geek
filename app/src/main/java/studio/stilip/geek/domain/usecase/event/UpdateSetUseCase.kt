package studio.stilip.geek.domain.usecase.event

import studio.stilip.geek.domain.entities.MemberScore
import studio.stilip.geek.domain.entities.Set
import studio.stilip.geek.domain.repository_interface.EventRepository
import javax.inject.Inject

class UpdateSetUseCase @Inject constructor(
    private val repository: EventRepository
) {
    suspend operator fun invoke(
        set: Set,
        membersScoresDeleted: List<MemberScore>,
        eventId: String
    ) = repository.updateSet(set, membersScoresDeleted, eventId)
}