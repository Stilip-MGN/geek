package studio.stilip.geek.domain.entities

data class SetWithList(
    val id: String = "",
    val title: String = "",
    val membersScores: List<UserWithScore> = emptyList()
)
