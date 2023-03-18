package studio.stilip.geek.domain.entities

data class Round(
    val id: String = "",
    val title: String = "",
    val scores: List<Score> = emptyList()
)
