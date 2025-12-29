package IrisClientAPI.Api

import IrisClientAPI.Api.Methods.*
import IrisClientAPI.Api.Seralizable.*
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import kotlinx.serialization.json.Json
import java.net.Proxy


class IrisApiClient(
    val botId: Long,
    val irisToken: String,
    val proxyClient: Proxy? = null,
    private val irisApiVersion: String = "0.5",
    internal val baseURL: String = "https://iris-tg.ru/api/${botId}_$irisToken/v$irisApiVersion"
) {
    /**
     * @param botId — уникальный индетификатор вашего Telegram бота.
     * @param irisToken — секретный ключ для подключения к IrisAPI. Для получения отправьте команду '+ирис коннект'
     * в ЛС https://t.me/iris_black_bot и следуйте инструкциям.
     */

    internal val json = Json { ignoreUnknownKeys = true }
    internal val logger = KotlinLogging.logger {}

    internal val httpClient = HttpClient(OkHttp) {
        engine {
            proxy = proxyClient

            config {
                followRedirects(true)
            }
        }
    }


    suspend fun giveSweets(
        count: Int, userId: Long, comment: String? = null, donateScore: Int? = null, withoutDonateScore: Boolean = true
    ): ResponseResult? {
        /**
         * Передача ирисок другому пользователю.
         *
         * @param count — число голд которое вы хотите передать.
         * @param userId — уникальный индетификатор Telegram получателя голд.
         * @param comment — комментарий к переводу, максимальная длина текста 128 символов. Необязательно к передаче.
         * @param donateScore — количество очков доната, которые будут использоваться в передаче.
         * @param withoutDonateScore — применять ли очки доната при передаче голд. Значение по умолчанию - true.
         */


        val method = "pocket/sweets/give"

        if (count <= 0) {
            throw CurrencyCountZeroException("Число ирисок не может быть нулевым или отрицательным")
        }

        if (comment != null && comment.length > 128) {
            throw LimitCommentLengthException("Максимальная длинна комментария не должна превышать 128 символов")
        }

        return giveCurrencyResponse(count, userId, comment, withoutDonateScore, method, donateScore)
    }


    suspend fun giveGold(
        count: Int, userId: Long, comment: String? = null, donateScore: Int? = null, withoutDonateScore: Boolean = true
    ): ResponseResult? {
        /**
         * Передача золотых ирисок другому пользователю.
         *
         * @param count — число голд которое вы хотите передать.
         * @param userId — уникальный индетификатор Telegram получателя голд.
         * @param comment — комментарий к переводу, максимальная длина текста 128 символов. Необязательно к передаче.
         * @param donateScore — количество очков доната, которые будут использоваться в передаче.
         * @param withoutDonateScore — применять ли очки доната при передаче голд. Значение по умолчанию - true.
         */

        val method = "pocket/gold/give"

        if (count <= 0) {
            throw CurrencyCountZeroException("Число не может быть нулевым или отрицательным")
        }

        if (comment != null && comment.length > 128) {
            throw LimitCommentLengthException("Максимальная длинна комментария не должна превышать 128 символов")
        }

        return giveCurrencyResponse(count, userId, comment, withoutDonateScore, method, donateScore)
    }


    suspend fun giveTgStars(userId: Long, count: Int, comment: String? = null): ResponseResult? {
        /**
         * Передача Telegram-Stars другому пользователю.
         *
         * @param count — число голд которое вы хотите передать.
         * @param userId — уникальный индетификатор Telegram получателя голд.
         * @param comment — комментарий к переводу, максимальная длина текста 128 символов. Необязательно к передаче.
         */

        val method = "pocket/tgstars/give"

        if (count <= 0) {
            throw CurrencyCountZeroException("Число не может быть нулевым или отрицательным")
        }

        if (comment != null && comment.length > 128) {
            throw LimitCommentLengthException("Максимальная длинна комментария не должна превышать 128 символов")
        }

        return giveCurrencyResponse(count, userId, comment, method = method)
    }


    suspend fun giveDonateScore(count: Int, userId: Long, comment: String? = null): ResponseResult? {
        /**
         * Передача очков доната другому пользователю.
         *
         * @param count — число голд которое вы хотите передать.
         * @param userId — уникальный индетификатор Telegram получателя голд.
         * @param comment — комментарий к переводу, максимальная длина текста 128 символов. Необязательно к передаче.
         */

        val method = "pocket/donate_score/give"

        if (count <= 0) {
            throw CurrencyCountZeroException("Число не может быть нулевым или отрицательным")
        }

        if (comment != null && comment.length > 128) {
            throw LimitCommentLengthException("Максимальная длинна комментария не должна превышать 128 символов")
        }

        return giveCurrencyResponse(count, userId, comment, false, method)
    }


    suspend fun getBalance(): BalanceSerialization? {
        /**
         * Получение баланса вашего бота.
         */

        val method = "pocket/balance"

        return getBalanceResponse(method)
    }


    suspend fun getSweetsHistory(offset: Int = 0, limit: Int = 200): HistorySerialization? {
        /**
         * Получение истории путешествий ирисок.
         *
         * @param limit — ограниченное число возвращаемых объектов из API.
         * @param offset — номер объекта с которого следует начать выдачу в API.
         */

        val method = "pocket/sweets/history"

        return getHistoryResponse(offset, limit, method)
    }


    suspend fun getGoldHistory(offset: Int = 0, limit: Int = 200): HistorySerialization? {
        /**
         * Получение истории путешествий голд.
         *
         * @param limit — ограниченное число возвращаемых объектов из API.
         * @param offset — номер объекта с которого следует начать выдачу в API.
         */

        val method = "pocket/gold/history"

        return getHistoryResponse(offset, limit, method)
    }


    suspend fun getDonateScoreHistory(offset: Int = 0, limit: Int = 200): HistorySerialization? {
        /**
         * Получение истории путешествий пончиков.
         *
         * @param limit — ограниченное число возвращаемых объектов из API.
         * @param offset — номер объекта с которого следует начать выдачу в API.
         */

        val method = "pocket/donate_score/history"
        return getHistoryResponse(offset, limit, method)
    }


    suspend fun getTgStarsHistory(offset: Int = 0, limit: Int = 200): HistorySerialization? {
        /**
         * Получение истории путешествий тг-звезд.
         *
         * @param limit — ограниченное число возвращаемых объектов из API.
         * @param offset — номер объекта с которого следует начать выдачу в API.
         */

        val method = "pocket/tgstars/history"
        return getHistoryResponse(offset, limit, method)
    }


    suspend fun enableOrDisablePocket(enable: Boolean = true): ResponseResult? {
        /**
         * Включение/отключение возможности открытия мешка бота пользователями.
         */

        val method = if (enable) "pocket/enable" else "pocket/disable"

        return enableDisablePocketResponse(method)
    }


    suspend fun enableOrDisableAllPocket(enable: Boolean = true): ResponseResult? {
        /**
         * Включение/отключение возможности передачи валюты боту для всех пользователяй.
         */

        val method = if (enable) "pocket/allow_all" else "pocket/deny_all"

        return enableDisablePocketResponse(method)
    }


    suspend fun allowOrDenyUserPocket(userId: Long, enable: Boolean = true): ResponseResult? {
        /**
         * Включение/отключение возможности передачи валюты боту для конкратного пользователя.
         *
         * @param userId — уникальный индетификатор Telegram получателя голд.
         **/

        val method = if (enable) "pocket/allow_user" else "pocket/deny_user"

        return allowDenyUserPocketResponse(userId, method)
    }


    suspend fun getUpdates(offset: Int = 0, limit: Int = 0): UpdatesSerialization? {
        /**
         * Получение логов обновлений.
         *
         * @param limit — ограниченное число возвращаемых объектов из API.
         * @param offset — номер объекта с которого следует начать выдачу в API.
         */

        val method = "getUpdates"
        return getUpdatesResponse(offset, limit, method)
    }


    suspend fun getIrisAgents(): List<Long>? {
        /**
         * Получение списка действующих агентов ириса.
         */

        val method = "iris_agents"
        return getIrisAgentsResponse(method)
    }


    fun generateDeepLink(currency: Currencies, count: Int, comment: String? = null): String {
        /**
         * Генерация deep-link для взаимодейстия с валютами бота.
         *
         * @param currency — валюта, на основе которой будет создана ссылка.
         * @param count — количество валюты.
         * @param comment — комментарий к переводу.
         */

        if (count <= 0) {
            throw CurrencyCountZeroException("Число не может быть нулевым или отрицательным")
        }

        if (comment != null) {
            if (comment.length > 128) {
                throw LimitCommentLengthException("Максимальная длинна комментария не должна превышать 128 символов")
            }

            val regex = "^[а-яa-zA-ZА-Я0-9_]+\$".toRegex()

            if (!regex.matches(comment)) {
                throw CommentNotPatternException("Комментарий может содержать только буквы, цифры и символ подчеркивания")
            }
        }

        var url = when (currency) {
            Currencies.GOLD -> "https://t.me/iris_black_bot?start=givegold_bot${botId}_$count"
            Currencies.SWEETS -> "https://t.me/iris_black_bot?start=give_bot${botId}_$count"
            Currencies.DONATE_SCOPE -> "https://t.me/iris_black_bot?start=givedonate_score_bot${botId}_$count"
            Currencies.TGSTARS -> "https://t.me/iris_cm_bot?start=givetgstars_bot${botId}_$count"
        }

        if (comment != null) url += "_$comment"
        return url
    }

    fun generateBotPermissionsDeepLink(permissions: List<BotPermissions>): String {
        /**
         * Генерация deep-link для выдачи разрешения боту
         *
         * @param permissions — список разрешенний бота, которые необходимо погрузить в deep-link
         */

        var url = "https://t.me/iris_black_bot?start=request_rights_$botId"

        for (permission in permissions) {
            url = url + "_${permission.name.lowercase()}"
        }
        return url
    }


    suspend fun checkUserReg(userId: Long): UserRegInfoSerialization? {
        /**
         * Получение даты первого появления во вселенной ириса.
         *
         * @param userId — уникальный индетификатор Telegram получателя голд.
         */

        val method = "user_info/reg"

        return getUserInfoResponse(userId, method)
    }


    suspend fun checkUserSpam(userId: Long): UserSpamInfoSerialization? {
        /**
         * Получение информации о нахождение пользователя в спам/гнор/скам базах ириса.
         *
         * @param userId — уникальный индетификатор Telegram получателя голд.
         */

        val method = "user_info/spam"

        return getUserInfoResponse(userId, method)
    }


    suspend fun checkUserActivity(userId: Long): UserActivityInfoSerialization? {
        /**
         * Получение статистики активности пользователя в чатах ириса.
         *
         * @param userId — уникальный индетификатор Telegram получателя голд.
         */

        val method = "user_info/activity"

        return getUserInfoResponse(userId, method)
    }


    suspend fun checkUserStars(userId: Long): UserStarsInfoSerialization? {
        /**
         * Получение информации о звездочности пользователя в ирисе.
         *
         * @param userId — уникальный индетификатор Telegram получателя голд.
         */

        val method = "user_info/stars"

        return getUserInfoResponse(userId, method)
    }


    suspend fun checkUserPocket(userId: Long): UserPocketInfoSerialization? {
        /**
         * Получение информации о мешке пользователя в ирисе.
         *
         * @param userId — уникальный индетификатор Telegram получателя голд.
         */

        val method = "user_info/pocket"

        return getUserInfoResponse(userId, method)
    }


    suspend fun buyTrade(price: Double, volume: Int): BuyTradeSerialization? {
        /**
         * Заявка на покупку ирис-голд.
         *
         * @param price — цена покупки. от 0.01 до 1_000_000. При желании купить ирис-голд "по рынку", указывайте максимальную цену.
         * @param volume — желаемое количество золотых ирисок для покупки.
         */

        val method = "trade/buy"

        if (price < 0.01 || price > 1000000.0) {
            throw TradesPriceException("Сумма должна быть в диапозоне от 0.01 до 1000000.0 ирисок.")
        }

        return buyTradeResponse(price, volume, method)
    }


    suspend fun sellTrade(price: Double, volume: Int): SellTradeSerialization? {
        /**
         * Заявка на продажу ирис-голд.
         *
         * @param price — цена продажи. от 0.01 до 1_000_000. При желании продать ирис-голд "по рынку", указывайте максимальную цену.
         * @param volume — желаемое количество золотых ирисок для продажи.
         */

        val method = "trade/sell"

        if (price < 0.01 || price > 1000000.0) {
            throw TradesPriceException("Сумма должна быть в диапозоне от 0.01 до 1000000.0 ирисок.")
        }

        return sellTradeResponse(price, volume, method)
    }


    suspend fun getOrdersTrade(): OrdersSerialization? {
        /**
         * Список заявок бота на Ирис-бирже.
         */

        val method = "trade/my_orders"

        return getOrdersResponse(method)
    }


    suspend fun cancelPriceTrade(price: Double, volume: Int): CanselTradeSerialization? {
        /**
         * Отменить все заявки по указанной цене.
         *
         * @param price — цена покупки. от 0.01 до 1_000_000.
         * @param volume — объём золотых ирисок для отмены. Если указанный объём равен или превышает объём выбранной
         * заявки, то заявка полностью снимается.
         */

        val method = "trade/cancel_price"

        if (price < 0.01 || price > 1000000.0) {
            throw TradesPriceException("Сумма должна быть в диапозоне от 0.01 до 1000000.0 ирисок.")
        }

        return cancelPriceTradeResponse(price, volume, method)
    }


    suspend fun cancelAllTrade(): CanselTradeSerialization? {
        /**
         * Отменить вообще все заявки бота.
         */

        val method = "trade/cancel_all"

        return cancelAllTradeResponse(method)
    }


    suspend fun cancelPartTrade(id: Int, volume: Int): CanselTradeSerialization? {
        /**
         * Отменить выбранную заявку частично.
         *
         * @param id — уникальный индитификатор заявки на Ирис-бирже.
         * @param volume — объём золотых ирисок для отмены. Если указанный объём равен или превышает объём выбранной заявки,
         *  то заявка полностью снимается.
         */

        val method = "trade/cancel_part"

        return cancelPartTradeResponse(id, volume, method)
    }


    suspend fun getOrderBookTrade(): OrdersSerialization? {
        /**
         * текущий стакан заявок Ирис-биржи.
         */

        val method = "trade/orderbook"

        return getOrdersResponse(method)
    }


    suspend fun getDealsTrade(id: Int = 0, limit: Int = 200): DealsSerialization? {
        /**
         *  История сделок на бирже ириса.
         *
         * @param id — уникальный индитификатор сделки, начиная с которой будет выдано limit записей.
         * По умолчанию 0 — выдаст последние limit сделок, совершённых на бирже.
         * @param limit — максимальное количество выдаваемых записей. Значения от 0 до 200. По умолчанию — 200.
         */

        val method = "trade/deals"
        return getDealsTradeResponse(id, limit, method)
    }


    suspend fun buyTgStars(count: Int): ResponseResult? {
        /**
         * Покупка Telegram-Stars за ириски.
         *
         * @param count - колличества Telegram-Stars для покупки.
         */

        val method = "pocket/tgstars/buy"

        return buyTgStarsResponse(count, method)
    }


    suspend fun getPriceTgStars(count: Int): PriceTgStarsSerialization? {
        /**
         * Оценка стоимости покупки Telegram-Stars.
         *
         * @param count — колличества Telegram-Stars для покупки.
         */

        val method = "pocket/tgstars/price"

        return getPriceTgStarsResponse(count, method)
    }


    suspend fun giveNFT(
        id: Int, nftName: String, userId: Long, comment: String?
    ): ResponseResult? {
        /**
         * Передача NFT между пользователями.
         *
         * @param id — уникальный индификатор NFT в системе ириса.
         * @param nftName — уникальное название NFT системы Telegram.
         * @param userId — уникальный индитификатор пользователя Telegram.
         * @param comment — комментарий к переводу. (необязателен).
         */

        val method = "nft/give"

        if (comment != null && comment.length > 128) {
            throw LimitCommentLengthException("Максимальная длинна комментария не должна превышать 128 символов")
        }

        return giveNFTResponse(id, nftName, userId, comment, method)
    }


    suspend fun getNFTInfo(id: Int, nftName: String): NFTInfoSerialization? {
        /**
         * Получение информации об NFT.
         *
         * @param id — уникальный индитификатор NFT в системе ириса.
         * @param nftName — уникальное название NFT системы Telegram.
         */

        val method = "nft/info"

        return getNFTInfoResponse(id, nftName, method)
    }


    suspend fun getNFTList(limit: Int = 200, offset: Int = 0): NFTListSerialization? {
        /**
         * Получение списка NFT.
         *
         * @param limit — ограниченное число возвращаемых объектов из API.
         * @param offset — номер объекта с которого следует начать выдачу в API.
         */

        val method = "nft/list"

        return getNFTListResponse(limit, offset, method)
    }


    suspend fun getNFTHistory(limit: Int = 200, offset: Int = 0): NFTHistorySerialization? {
        /**
         * Получение истории движения NFT.
         *
         * @param limit — ограниченное число возвращаемых объектов из API.
         * @param offset — номер объекта с которого следует начать выдачу в API.
         * */

        val method = "nft/history"

        return getNFTHistory(limit, offset, method)
    }
}