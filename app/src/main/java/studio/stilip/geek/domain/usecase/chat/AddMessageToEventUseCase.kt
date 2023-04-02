package studio.stilip.geek.domain.usecase.chat

import studio.stilip.geek.domain.entities.Message
import studio.stilip.geek.domain.repository_interface.ChatRepository
import javax.inject.Inject

class AddMessageToEventUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(eventId: String, message: Message) =
        repository.addMessageToEvent(eventId, message)
}