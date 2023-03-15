package studio.stilip.geek.domain.usecase.event

import studio.stilip.geek.domain.repository_interface.EventRepository
import javax.inject.Inject

class GetMembersByEventIdUseCase @Inject constructor(
    private val repository: EventRepository
) {
    operator fun invoke(id: String) = repository.getMembersByEventId(id)
}