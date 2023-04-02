package studio.stilip.geek.domain.entities

data class Message(
    val id: String,
    val createdAt: Long,
    val createdBy: String,
    val text: String,
)
