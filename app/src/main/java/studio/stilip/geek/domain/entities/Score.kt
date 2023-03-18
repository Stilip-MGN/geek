package studio.stilip.geek.domain.entities

data class Score(
    val id: String = "",
    val memberId: String = "",
    val score: Int = 0,
    val members: List<User> = emptyList()
)
