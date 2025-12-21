package IrisClientAPI.Api

import IrisClientAPI.Api.Seralizable.*
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.net.Proxy


class IrisApiClient(
    val botId: Long,
    val irisToken: String,
    val proxyClient: Proxy? = null,
    private val irisApiVersion: String = "0.4",
    private val baseURL: String = "https://iris-tg.ru/api/${botId}_$irisToken/v$irisApiVersion"
) {
    /**
     * - botId - Уникальный индетификатор вашего Telegram бота.
     * - irisToken - Секретный ключ для подключения к IrisAPI. Для получения отправьте команду '+ирис коннект'
     * в ЛС https://t.me/iris_black_bot и следуйте инструкциям.
     */

    private val json = Json { ignoreUnknownKeys = true }
    private val logger = KotlinLogging.logger {}

    private val httpClient = HttpClient(OkHttp) {
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
         * count - Число ирисок которое вы хотите передать.
         * userId Уникальный индетификатор Telegram получателя ирисок.
         * comment - Комментарий к переводу, максимальная длина текста 128 символов. Необязательно к передаче.
         * donateScore - Количество очков доната, которые будут использоваться в передаче.
         * withoutDonateScore - Применять ли очки доната при передаче ирисок. Значение по умолчанию - true
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
         * count - Число голд которое вы хотите передать.
         * userId Уникальный индетификатор Telegram получателя голд.
         * comment - Комментарий к переводу, максимальная длина текста 128 символов. Необязательно к передаче.
         * donateScore - Количество очков доната, которые будут использоваться в передаче.
         * withoutDonateScore - Применять ли очки доната при передаче голд. Значение по умолчанию - true
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
         * count - Число тг-звезд которое вы хотите передать.
         * userId Уникальный индетификатор Telegram получателя тг-звезд.
         * comment - Комментарий к переводу, максимальная длина текста 128 символов. Необязательно к передаче.
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
         * count - Число голд которое вы хотите передать.
         * userId Уникальный индетификатор Telegram получателя голд.
         * comment - Комментарий к переводу, максимальная длина текста 128 символов. Необязательно к передаче.
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
         * Получение истории путешествий ирисок
         */

        val method = "pocket/sweets/history"

        return getHistoryResponse(offset, limit, method)
    }


    suspend fun getGoldHistory(offset: Int = 0, limit: Int = 200): HistorySerialization? {
        /**
         * Получение истории путешествий голд
         */

        val method = "pocket/gold/history"

        return getHistoryResponse(offset, limit, method)
    }


    suspend fun getDonateScoreHistory(offset: Int = 0, limit: Int = 200): HistorySerialization? {
        /**
         * Получение истории путешествий пончиков
         */

        val method = "pocket/donate_score/history"
        return getHistoryResponse(offset, limit, method)
    }


    suspend fun getTgStarsHistory(offset: Int = 0, limit: Int = 200): HistorySerialization? {
        /**
         * Получение истории путешествий тг-звезд
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


    suspend fun allowOrDenyUserPocket(userId: Long, enable: Boolean): ResponseResult? {
        /**
         * userId Уникальный индетификатор Telegram получателя голд.
         *
         * Включение/отключение возможности передачи валюты боту для конкратного пользователя.
         */

        val method = if (enable) "pocket/allow_user" else "pocket/deny_user"

        return allowDenyUserPocketResponse(userId, method)
    }


    suspend fun getUpdates(offset: Int = 0, limit: Int = 0): UpdatesSerialization? {
        /**
         * Получение логов обновлений
         */

        val method = "getUpdates"
        return getUpdatesResponse(offset, limit, method)
    }


    suspend fun getIrisAgents(): List<Long>? {
        /**
         * Получение списка действующих агентов ириса
         */

        val method = "iris_agents"
        return getIrisAgentsResponse(method)
    }


    fun generateDeepLink(currency: Currencies, count: Int, comment: String? = null): String {
        /**
         * Генерация deep-link для взаимодейстия с валютами бота
         * currency - валюта, на основе которой будет создана ссылка
         * count - количество валюты
         * comment - комментарий к переводу
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
         * permissions - список разрешенний бота, которые необходимо погрузить в deep-link
         * Генерация deep-link для выдачи разрешения боту
         */

        var url = "https://t.me/iris_black_bot?start=request_rights_$botId"

        for (permission in permissions) {
            url = url + "_${permission.name.lowercase()}"
        }
        return url
    }


    suspend fun checkUserReg(userId: Long): UserRegInfoSerialization? {
        /**
         * userId Уникальный индетификатор Telegram получателя голд.
         * Получение даты первого появления во вселенной ириса
         */

        val method = "user_info/reg"

        return getUserInfoResponse(userId, method)
    }


    suspend fun checkUserSpam(userId: Long): UserSpamInfoSerialization? {
        /**
         * userId Уникальный индетификатор Telegram получателя голд.
         * Получение информации о нахождение пользователя в спам/гнор/скам базах ириса
         */

        val method = "user_info/spam"

        return getUserInfoResponse(userId, method)
    }


    suspend fun checkUserActivity(userId: Long): UserActivityInfoSerialization? {
        /**
         * userId Уникальный индетификатор Telegram получателя голд.
         * Получение статистики активности пользователя в чатах ириса
         */

        val method = "user_info/activity"

        return getUserInfoResponse(userId, method)
    }


    suspend fun checkUserStars(userId: Long): UserStarsInfoSerialization? {
        /**
         * userId Уникальный индетификатор Telegram получателя голд.
         * Получение информации о звездочности пользователя в ирисе
         */

        val method = "user_info/stars"

        return getUserInfoResponse(userId, method)
    }


    suspend fun checkUserPocket(userId: Long): UserPocketInfoSerialization? {
        /**
         * userId Уникальный индетификатор Telegram получателя голд.
         * Получение информации о мешке пользователя в ирисе
         */

        val method = "user_info/pocket"

        return getUserInfoResponse(userId, method)
    }


    suspend fun buyTrade(price: Double, volume: Int): BuyTradeSerialization? {
        /**
         * price — цена покупки. от 0.01 до 1_000_000. При желании купить ирис-голд "по рынку", указывайте максимальную цену.
         * volume — желаемое количество золотых ирисок для покупки.
         * Заявка на покупку ирис-голд.
         */

        val method = "trade/buy"

        if (price < 0.01 || price > 1000000.0) {
            throw TradesPriceException("Сумма должна быть в диапозоне от 0.01 до 1000000.0 ирисок.")
        }

        return buyTradeResponse(price, volume, method)
    }


    suspend fun sellTrade(price: Double, volume: Int): SellTradeSerialization? {
        /**
         * price — цена продажи. от 0.01 до 1_000_000. При желании продать ирис-голд "по рынку", указывайте максимальную цену.
         * volume — желаемое количество золотых ирисок для продажи.
         * Заявка на продажу ирис-голд.
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
         * price — цена покупки. от 0.01 до 1_000_000.
         * volume — объём золотых ирисок для отмены. Если указанный объём равен или превышает объём выбранной заявки, то заявка полностью снимается
         * Отменить все заявки по указанной цене.
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
         * id — ид заявки на Ирис-бирже.
         * volume — объём золотых ирисок для отмены. Если указанный объём равен или превышает объём выбранной заявки, то заявка полностью снимается
         * Отменить выбранную заявку частично.
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
         *  история сделок на бирже ириса.
         * - id — id сделки, начиная с которой будет выдано limit записей.  По умолчанию 0 — выдаст последние limit сделок, совершённых на бирже
         * - limit — максимальное количество выдаваемых записей. Значения от 0 до 200. По умолчанию — 200
         */

        val method = "trade/deals"
        return getDealsTradeResponse(id, limit, method)
    }


    suspend fun buyTgStars(count: Int): ResponseResult? {
        /**
         * Покупка Telegram звезд за ириски.
         * count - колличество тг-звезд для покупки.
         */

        val method = "pocket/tgstars/buy"

        return buyTgStarsResponse(count, method)
    }


    suspend fun getPriceTgStars(count: Int): PriceTgStarsSerialization? {
        /**
         * Оценка стоимости покупки тг-звёзд.
         * count - колличество тг-звезд для покупки.
         */

        val method = "pocket/tgstars/price"

        return getPriceTgStarsResponse(count, method)
    }


    private suspend fun cancelPartTradeResponse(id: Int, volume: Int, method: String): CanselTradeSerialization? {
        return withContext(Dispatchers.IO) {
            try {
                val response: HttpResponse = httpClient.get("$baseURL/$method") {
                    parameter("id", id)
                    parameter("volume", volume)
                }

                if (response.status == HttpStatusCode.OK) {
                    val jsonResult = response.bodyAsText()
                    json.decodeFromString<CanselTradeSerialization>(jsonResult)
                } else throw IrisResponseException("${response.bodyAsText()} (${response.status.value})")

            } catch (e: IrisResponseException) {
                logger.error { "Ошибка при попытке отмены заявки бирже: $e" }
                null
            }
        }
    }


    private suspend fun cancelAllTradeResponse(method: String): CanselTradeSerialization? {
        return withContext(Dispatchers.IO) {
            try {
                val response: HttpResponse = httpClient.get("$baseURL/$method")

                if (response.status == HttpStatusCode.OK) {
                    val jsonResult = response.bodyAsText()
                    json.decodeFromString<CanselTradeSerialization>(jsonResult)
                } else throw IrisResponseException("${response.bodyAsText()} (${response.status.value})")

            } catch (e: IrisResponseException) {
                logger.error { "Ошибка при попытке отмены заявки бирже: $e" }
                null
            }
        }
    }


    private suspend fun cancelPriceTradeResponse(price: Double, volume: Int, method: String): CanselTradeSerialization? {
        return withContext(Dispatchers.IO) {
            try {
                val response: HttpResponse = httpClient.get("$baseURL/$method") {
                    parameter("price", price)
                    parameter("volume", volume)
                }

                if (response.status == HttpStatusCode.OK) {
                    val jsonResult = response.bodyAsText()
                    json.decodeFromString<CanselTradeSerialization>(jsonResult)
                } else throw IrisResponseException("${response.bodyAsText()} (${response.status.value})")

            } catch (e: IrisResponseException) {
                logger.error { "Ошибка при попытке отмены заявки бирже: $e" }
                null
            }
        }
    }


    private suspend fun getOrdersResponse(method: String): OrdersSerialization? {
        return withContext(Dispatchers.IO) {
            try {
                val response: HttpResponse = httpClient.get("$baseURL/$method")

                if (response.status == HttpStatusCode.OK) {
                    val jsonResult = response.bodyAsText()
                    json.decodeFromString<OrdersSerialization>(jsonResult)
                } else throw IrisResponseException("${response.bodyAsText()} (${response.status.value})")

            } catch (e: IrisResponseException) {
                logger.error { "Ошибка при попытке создание заявки на покупку ирис-голд: $e" }
                null
            }
        }
    }


    private suspend fun buyTradeResponse(price: Double, volume: Int, method: String): BuyTradeSerialization? {
        return withContext(Dispatchers.IO) {
            try {
                val response: HttpResponse = httpClient.get("$baseURL/$method") {
                    parameter("price", price)
                    parameter("volume", volume)
                }

                if (response.status == HttpStatusCode.OK) {
                    val jsonResult = response.bodyAsText()
                    json.decodeFromString<BuyTradeSerialization>(jsonResult)
                } else throw IrisResponseException("${response.bodyAsText()} (${response.status.value})")

            } catch (e: IrisResponseException) {
                logger.error { "Ошибка при попытке создание заявки на покупку ирис-голд: $e" }
                null
            }
        }
    }


    private suspend fun sellTradeResponse(price: Double, volume: Int, method: String): SellTradeSerialization? {
        return withContext(Dispatchers.IO) {
            try {
                val response: HttpResponse = httpClient.get("$baseURL/$method") {
                    parameter("price", price)
                    parameter("volume", volume)
                }

                if (response.status == HttpStatusCode.OK) {
                    val jsonResult = response.bodyAsText()
                    json.decodeFromString<SellTradeSerialization>(jsonResult)
                } else throw IrisResponseException("${response.bodyAsText()} (${response.status.value})")

            } catch (e: IrisResponseException) {
                logger.error { "Ошибка при попытке создание заявки на покупку ирис-голд: $e" }
                null
            }
        }
    }


    private suspend fun getDealsTradeResponse(id: Int, limit: Int, method: String): DealsSerialization? {
        return withContext(Dispatchers.IO) {
            try {
                val response: HttpResponse = httpClient.post("$baseURL/$method") {
                    parameter("id", id)
                    parameter("limit", limit)
                }

                if (response.status == HttpStatusCode.OK) {
                    val jsonResult = response.bodyAsText()
                    json.decodeFromString<DealsSerialization>(jsonResult)
                } else {
                    ResponseResult(
                        result = 0, error = ApiError(
                            code = response.status.value, description = response.bodyAsText()
                        )
                    )
                    throw IrisResponseException("${response.bodyAsText()} (${response.status.value})")
                }

            } catch (e: IrisResponseException) {
                logger.error { "Ошибка при попытке переключить доступ к переводам $e" }
                null
            }
        }
    }


    private suspend fun <T> getUserInfoResponse(userId: Long, method: String): T? {
        return withContext(Dispatchers.IO) {
            try {
                val response: HttpResponse = httpClient.post("$baseURL/$method") {
                    parameter("user_id", userId)
                }

                if (response.status == HttpStatusCode.OK) {
                    val jsonResult = response.bodyAsText()

                    val result = when (method) {
                        BotPermissions.REG.getValue() -> json.decodeFromString<UserRegInfoSerialization>(jsonResult) as T
                        BotPermissions.ACTIVITY.getValue() -> json.decodeFromString<UserActivityInfoSerialization>(
                            jsonResult
                        ) as T

                        BotPermissions.SPAM.getValue() -> json.decodeFromString<UserSpamInfoSerialization>(jsonResult) as T
                        BotPermissions.STARS.getValue() -> json.decodeFromString<UserStarsInfoSerialization>(jsonResult) as T
                        BotPermissions.POCKET.getValue() -> json.decodeFromString<UserPocketInfoSerialization>(
                            jsonResult
                        ) as T

                        else -> throw IllegalArgumentException("Аргумент не найден: $method")
                    }
                    result
                } else throw IrisResponseException("${response.bodyAsText()} (${response.status.value})")

            } catch (e: IrisResponseException) {
                logger.error { "Ошибка при попытке получить информацию о пользователе $e" }
                null
            }
        }
    }


    private suspend fun allowDenyUserPocketResponse(userId: Long, method: String): ResponseResult? {
        return withContext(Dispatchers.IO) {
            try {
                val response: HttpResponse = httpClient.post("$baseURL/$method") {
                    parameter("user_id", userId)
                }

                if (response.status == HttpStatusCode.OK) {
                    val jsonResult = response.bodyAsText()
                    json.decodeFromString<ResponseResult>(jsonResult)
                } else {
                    ResponseResult(
                        result = 0, error = ApiError(
                            code = response.status.value, description = response.bodyAsText()
                        )
                    )
                    throw IrisResponseException("${response.bodyAsText()} (${response.status.value})")
                }

            } catch (e: IrisResponseException) {
                logger.error { "Ошибка при попытке переключить доступ к переводам $e" }
                null
            }
        }
    }


    private suspend fun enableDisablePocketResponse(method: String): ResponseResult? {
        return withContext(Dispatchers.IO) {
            try {
                val response: HttpResponse = httpClient.post("$baseURL/$method")

                if (response.status == HttpStatusCode.OK) {
                    val jsonResult = response.bodyAsText()
                    json.decodeFromString<ResponseResult>(jsonResult)
                } else {
                    ResponseResult(
                        result = 0, error = ApiError(
                            code = response.status.value, description = response.bodyAsText()
                        )
                    )
                    throw IrisResponseException("${response.bodyAsText()} (${response.status.value})")
                }

            } catch (e: IrisResponseException) {
                logger.error { "Ошибка при попытке переключить доступ к мешку $e" }
                null
            }
        }
    }


    private suspend fun giveCurrencyResponse(
        count: Int,
        userId: Long,
        comment: String? = null,
        withoutDonateScore: Boolean? = null,
        method: String,
        donateScore: Int? = null
    ): ResponseResult? {
        return withContext(Dispatchers.IO) {
            try {
                val response: HttpResponse = httpClient.post("$baseURL/$method") {
                    parameter("amount", count)
                    parameter("user_id", userId)
                    parameter("donate_score", donateScore)
                    parameter("comment", comment)
                    parameter("without_donate_score", withoutDonateScore)
                }

                if (response.status == HttpStatusCode.OK) {
                    val jsonResult = response.bodyAsText()
                    json.decodeFromString<ResponseResult>(jsonResult)
                } else {
                    ResponseResult(
                        result = 0, error = ApiError(
                            code = response.status.value, description = response.bodyAsText()
                        )
                    )
                    throw IrisResponseException("${response.bodyAsText()} (${response.status.value})")
                }

            } catch (e: IrisResponseException) {
                logger.error { "Ошибка при попытке передать валюту $e" }
                null
            }
        }
    }


    private suspend fun getBalanceResponse(method: String): BalanceSerialization? {
        return withContext(Dispatchers.IO) {
            try {
                val response: HttpResponse = httpClient.get("$baseURL/$method")

                if (response.status == HttpStatusCode.OK) {
                    val jsonResult = response.bodyAsText()
                    json.decodeFromString<BalanceSerialization>(jsonResult)
                } else throw IrisResponseException("${response.bodyAsText()} (${response.status.value})")

            } catch (e: IrisResponseException) {
                logger.error { "Ошибка при получение баланса бота $e" }
                null
            }
        }
    }


    private suspend fun getHistoryResponse(offset: Int, limit: Int, method: String): HistorySerialization? {
        return withContext(Dispatchers.IO) {
            try {
                val response: HttpResponse = httpClient.get("$baseURL/$method") {
                    parameter("offset", offset)
                    parameter("limit", limit)
                }

                if (response.status == HttpStatusCode.OK) {
                    val jsonResult = response.bodyAsText()
                    json.decodeFromString<HistorySerialization>(jsonResult)
                } else throw IrisResponseException("${response.bodyAsText()} (${response.status.value})")

            } catch (e: IrisResponseException) {
                logger.error { "Ошибка при получение истории обмена валют $e" }
                null
            }
        }
    }


    private suspend fun getUpdatesResponse(offset: Int, limit: Int, method: String): UpdatesSerialization? {
        return withContext(Dispatchers.IO) {
            try {
                val response: HttpResponse = httpClient.post("$baseURL/$method") {
                    parameter("offset", offset)
                    parameter("limit", limit)
                }

                if (response.status == HttpStatusCode.OK) {
                    val jsonResult = response.bodyAsText()
                    json.decodeFromString<UpdatesSerialization>(jsonResult)
                } else throw IrisResponseException("${response.bodyAsText()} (${response.status.value})")

            } catch (e: IrisResponseException) {
                logger.error { "Ошибка при попытке переключить доступ к переводам $e" }
                null
            }
        }
    }


    private suspend fun getIrisAgentsResponse(method: String): List<Long>? {
        return withContext(Dispatchers.IO) {
            try {
                val response: HttpResponse = httpClient.get("$baseURL/$method")

                if (response.status == HttpStatusCode.OK) {
                    val jsonResult = response.bodyAsText()
                    json.decodeFromString<List<Long>>(jsonResult)
                } else throw IrisResponseException("${response.bodyAsText()} (${response.status.value})")

            } catch (e: IrisResponseException) {
                logger.error { "Ошибка при получение истории обмена валют $e" }
                null
            }
        }
    }


    private suspend fun buyTgStarsResponse(count: Int, method: String): ResponseResult? {
        return withContext(Dispatchers.IO) {
            try {
                val response: HttpResponse = httpClient.get("$baseURL/$method") {
                    parameter("tgstars", count)
                }

                if (response.status == HttpStatusCode.OK) {
                    val jsonResult = response.bodyAsText()
                    json.decodeFromString<ResponseResult>(jsonResult)
                } else throw IrisResponseException("${response.bodyAsText()} (${response.status.value})")

            } catch (e: IrisResponseException) {
                logger.error { "Ошибка при покупке тг-звезд: $e" }
                null
            }
        }
    }


    private suspend fun getPriceTgStarsResponse(count: Int, method: String): PriceTgStarsSerialization? {
        return withContext(Dispatchers.IO) {
            try {
                val response: HttpResponse = httpClient.get("$baseURL/$method") {
                    parameter("amount", count)
                }

                if (response.status == HttpStatusCode.OK) {
                    val jsonResult = response.bodyAsText()

                    json.decodeFromString<PriceTgStarsSerialization>(jsonResult)
                } else throw IrisResponseException("${response.bodyAsText()} (${response.status.value})")

            } catch (e: IrisResponseException) {
                logger.error { "Ошибка при покупке тг-звезд: $e" }
                null
            }
        }
    }
}