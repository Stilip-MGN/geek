package studio.stilip.geek.domain.usecase.event

import studio.stilip.geek.domain.repository_interface.EventRepository
import javax.inject.Inject

class CreateRoundNewUseCase @Inject constructor(
    private val repository: EventRepository
) {
    suspend operator fun invoke(title: String, eventId: String) =
        repository.createRoundNew(title, eventId)
}