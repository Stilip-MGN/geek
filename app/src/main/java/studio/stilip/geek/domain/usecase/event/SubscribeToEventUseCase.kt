package studio.stilip.geek.domain.usecase.event

import studio.stilip.geek.domain.entities.User
import studio.stilip.geek.domain.repository_interface.EventRepository
import javax.inject.Inject

class SubscribeToEventUseCase @Inject constructor(
    private val repository: EventRepository
) {
    suspend operator fun invoke(user: User, eventId: String) =
        repository.subscribeToEvent(user, eventId)
}