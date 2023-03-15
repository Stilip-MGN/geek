package studio.stilip.geek.data

object UserCacheManager {
    private var userId = ""

    fun setUserId(id: String) {
        this.userId = id
    }

    fun getUserId(): String = userId
}