package studio.stilip.geek.domain.entities

data class Message(
    val id: String = "",
    val createdAt: Long = 0L,
    val createdBy: String = "",
    val text: String = "",
)
