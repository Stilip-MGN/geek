package studio.stilip.geek.domain.entities

data class RoundNew(
    val id: String = "",
    val title: String = "",
    val setsIds: List<String> = emptyList()
)
