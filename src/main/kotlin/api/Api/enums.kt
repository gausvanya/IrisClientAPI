package IrisClientAPI.Api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


enum class Currencies(private val currency: String) {
    GOLD("gold"),
    SWEETS("sweets"),
    DONATE_SCOPE("donate_score"),
    TGSTARS("tgstars");

    fun getValue(): String { return currency }
}


enum class BotPermissions(private val info: String) {
    REG("user_info/reg"),
    ACTIVITY("user_info/activity"),
    SPAM("user_info/spam"),
    STARS("user_info/stars"),
    POCKET("user_info/pocket");

    fun getValue(): String { return info }
}


@Serializable
enum class UpdateTypes {
    @SerialName("tg_stars_log")
    TG_STARS_LOG,

    @SerialName("donate_score_log")
    DONATE_SCORE_LOG,

    @SerialName("sweets_log")
    SWEETS_LOG,

    @SerialName("gold_log")
    GOLD_LOG
}

@Serializable
enum class HistoryTypes() {
    // Валюта была отправлена пользователю.
    @SerialName("send")
    SEND,

    // Валюта была получена пользователем.
    @SerialName("receive")
    RECEIVE,

    // Отпрака очков доната при передаче ирисок/голд.
    @SerialName("send_with")
    SEND_WITH,

    // Получение очков доната при передаче ирисок/голд.
    @SerialName("receive_with")
    RECEIVE_WITH,

    // Ириски получены в результате распределения дивидендов за владение ирис-голд.
    @SerialName("dividends")
    DIVIDENDS,

    // Обмен ирисок на тг-звезды.
    @SerialName("purchase")
    PURCHASE,

    // Ириски потрачены на конвертацию в тг-звёзды.
    @SerialName("purchase_tgstars")
    PURCHASE_TGSTARS,

    // Операции на ирис-бирже.
    @SerialName("trade")
    TRADE,

    // Неизвестный тип
    @SerialName("unknown")
    UNKNOWN
}