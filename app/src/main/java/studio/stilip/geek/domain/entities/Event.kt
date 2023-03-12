package studio.stilip.geek.domain.entities

data class Event(
    val id: String = "",
    val eventName: String = "",
    val gameId: String = "",
    val gameName: String = "",
    val place: String = "",
    val date: String = "",
    val description: String = "",
    val maxMembers: Int = 0,
)
