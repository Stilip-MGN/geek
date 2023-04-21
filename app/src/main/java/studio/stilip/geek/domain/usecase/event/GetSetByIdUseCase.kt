package studio.stilip.geek.domain.usecase.event

import studio.stilip.geek.domain.repository_interface.EventRepository
import javax.inject.Inject

class GetSetByIdUseCase @Inject constructor(
    private val repository: EventRepository
) {
    operator fun invoke(id: String) = repository.getSetById(id)
}