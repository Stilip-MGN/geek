package studio.stilip.geek.domain.usecase.event

import studio.stilip.geek.domain.repository_interface.EventRepository
import javax.inject.Inject

class ReplaceMemberInScoreUseCase @Inject constructor(
    private val repository: EventRepository
) {
    suspend operator fun invoke(
        eventId: String,
        roundId: String,
        scoreId: String,
        userId: String
    ) = repository.replaceMemberInScore(eventId, roundId, scoreId, userId)
}