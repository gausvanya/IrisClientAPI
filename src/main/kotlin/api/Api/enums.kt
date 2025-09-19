package IrisClientAPI.Api


enum class Currencies(private val currency: String) {
    GOLD("gold"),
    SWEETS("sweets"),
    DONATE_SCOPE("donate_score");
}


enum class BotPermissions(private val info: String) {
    REG("user_info/reg"),
    ACTIVITY("user_info/activity"),
    SPAM("user_info/spam"),
    STARS("user_info/stars"),
    POCKET("user_info/pocket");

    fun getValue(): String { return info }
}
