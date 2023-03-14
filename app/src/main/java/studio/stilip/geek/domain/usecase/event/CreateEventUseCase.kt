package studio.stilip.geek.domain.usecase.event

import studio.stilip.geek.domain.entities.Event
import studio.stilip.geek.domain.repository_interface.EventRepository
import javax.inject.Inject

class CreateEventUseCase @Inject constructor(
    private val repository: EventRepository
) {
    suspend operator fun invoke(event: Event) = repository.createEvent(event)
}