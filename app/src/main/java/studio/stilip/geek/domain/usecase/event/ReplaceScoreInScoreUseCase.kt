package studio.stilip.geek.domain.usecase.event

import studio.stilip.geek.domain.repository_interface.EventRepository
import javax.inject.Inject

class ReplaceScoreInScoreUseCase @Inject constructor(
    private val repository: EventRepository
) {
    suspend operator fun invoke(
        eventId: String,
        roundId: String,
        scoreId: String,
        score: Int
    ) = repository.replaceScoreInScore(eventId, roundId, scoreId, score)
}