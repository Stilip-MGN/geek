package studio.stilip.geek.domain.usecase.event

import studio.stilip.geek.domain.repository_interface.EventRepository
import javax.inject.Inject

class GetAllEventsUseCase @Inject constructor(
    private val repository: EventRepository
) {
    operator fun invoke() = repository.getAllEvents()
}