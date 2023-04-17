package studio.stilip.geek.domain.entities

data class UserWithScore(
    val id: String = "",
    val user: User = User(),
    val score: Int = 0,
)
