package studio.stilip.geek.domain.entities

data class Set(
    val id: String = "",
    val title: String = "",
    val membersScores: List<MemberScore> = emptyList()
)
