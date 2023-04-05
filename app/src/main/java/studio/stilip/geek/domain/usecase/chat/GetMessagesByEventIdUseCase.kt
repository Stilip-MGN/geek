package studio.stilip.geek.domain.usecase.chat

import studio.stilip.geek.domain.repository_interface.ChatRepository
import javax.inject.Inject

class GetMessagesByEventIdUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    operator fun invoke(eventId: String) = repository.getMessagesByEventId(eventId)
}