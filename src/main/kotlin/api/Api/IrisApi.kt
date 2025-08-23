package api.Api

import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.client.engine.okhttp.*

import kotlinx.serialization.json.Json

private const val apiVersion = "0.1"



class IrisApiClient(
    val botId: Long,
    val irisToken: String,
    private val baseURL: String = "https://iris-tg.ru/api/${botId}_$irisToken/v$apiVersion"
) {
    /**
     * botId - Уникальный индитификатор вашего Telegram бота.
     * irisToken - Секретный ключ для подключения к IrisAPI. Для получения отправьте команду '+ирис коннект'
     * в ЛС https://t.me/iris_black_bot и следуйте инструкциям.
     */


    private val json = Json { ignoreUnknownKeys = true }
    private val logger = KotlinLogging.logger {}
    private val httpClient = HttpClient(OkHttp) {
        engine {
            config {
                followRedirects(true)
            }
        }
    }


    suspend fun giveSweets(
        count: Int,
        userId: Long,
        comment: String? = null,
        withoutDonateScore: Boolean = true
    ): ResponseResult? {
        /**
         * count - Число ирисок которое вы хотите передать.
         * userId Уникальный индетификатор Telegram получателя ирисок.
         * comment - Комментарий к переводу, максимальная длина текста 128 символов. Необязательно к передаче.
         * withoutDonateScore - Применять ли очки доната при передаче ирисок. Значение по умолчанию - true
         */

        val method = "pocket/sweets/give"

        if (count <= 0) {
            throw CurrencyCountZeroException("Число ирисок не может быть нулевым или отрицательным")
        }

        if (comment != null && comment.length > 128) {
            throw LimitCommentLengthException("Максимальная длинна комментария не должна превышать 128 символов")

        }

        return giveCurrency(count, userId, comment, withoutDonateScore, method)
    }


    suspend fun giveGold(
        count: Int,
        userId: Long,
        comment: String? = null,
        withoutDonateScore: Boolean = true
    ): ResponseResult? {
        /**
         * count - Число голд которое вы хотите передать.
         * userId Уникальный индетификатор Telegram получателя голд.
         * comment - Комментарий к переводу, максимальная длина текста 128 символов. Необязательно к передаче.
         * withoutDonateScore - Применять ли очки доната при передаче голд. Значение по умолчанию - true
         */

        val method = "pocket/gold/give"

        if (count <= 0) {
            throw CurrencyCountZeroException("Число голд не может быть нулевым или отрицательным")
        }

        return giveCurrency(count, userId, comment, withoutDonateScore, method)
    }


    suspend fun balance(): Any? {
        /**
         * Получение баланса вашего бота.
          */

        val method = "pocket/balance"

        return getBalance(method)
    }


    suspend fun sweetsHistory(offset: Int = 0): Any? {
        /**
         * Получение истории путешествий ирисок
         */

        val method = "pocket/sweets/history"

        return getHistory(offset, method)
    }


    suspend fun goldHistory(offset: Int = 0): Any? {
        /**
         * Получение истории путешествий голд
         */

        val method = "pocket/gold/history"

        return getHistory(offset, method)
    }


    suspend fun enableOrDisablePocket(enable: Boolean = true): ResponseResult? {
        /**
         * Включение/отключение возможности открытия мешка бота пользователями.
         */

        val method = if (enable) "pocket/enable" else "pocket/disable"

        return enableDisablePocket(method)
    }


    suspend fun enableOrDisableAllPocket(enable: Boolean = true): ResponseResult? {
        /**
         * Включение/отключение возможности передачи валюты боту для всех пользователяй.
         */

        val method = if (enable) "pocket/allow_all" else "pocket/deny_all"

        return enableDisablePocket(method)
    }


    suspend fun allowOrDenyUserPocket(userId: Long, enable: Boolean): ResponseResult? {
        /**
         * userId Уникальный индетификатор Telegram получателя голд.
         *
         * Включение/отключение возможности передачи валюты боту для конкратного пользователя.
         */

        val method = if (enable) "pocket/allow_user" else "pocket/deny_user"

        return allowDenyUserPocket(userId, method)
    }


    private suspend fun allowDenyUserPocket(userId: Long, method: String): ResponseResult? {
        return withContext(Dispatchers.IO) {

            try {
                val response: HttpResponse = httpClient.post("$baseURL/$method") {
                    parameter("user_id", userId)
                }

                if (response.status == HttpStatusCode.OK) {
                    val jsonResult = response.bodyAsText()
                    json.decodeFromString<ResponseResult>(jsonResult)
                } else {
                    ResponseResult(result = false, error = APIError(
                        code = response.status.toString().toInt(), description = response.bodyAsText())
                    )
                }

            } catch (e: IrisResponseException) {
                logger.error { "Ошибка при попытке переключить доступ к переводам $e" }
                null
            }
        }
    }


    private suspend fun enableDisablePocket(method: String): ResponseResult? {
        return withContext(Dispatchers.IO) {

            try {
                val response: HttpResponse = httpClient.post("$baseURL/$method")

                if (response.status == HttpStatusCode.OK) {
                    val jsonResult = response.bodyAsText()
                    json.decodeFromString<ResponseResult>(jsonResult)
                } else {
                    ResponseResult(result = false, error = APIError(
                        code = response.status.toString().toInt(), description = response.bodyAsText())
                    )
                }

            } catch (e: IrisResponseException) {
                logger.error { "Ошибка при попытке переключить доступ к мешку $e" }
                null
            }
        }
    }


    private suspend fun giveCurrency (
        count: Int,
        userId: Long,
        comment: String?,
        withoutDonateScore: Boolean,
        method: String
    ) : ResponseResult?
    {
        return withContext(Dispatchers.IO) {

            val currency = if (method == "pocket/gold/give") "gold" else "sweets"

            try {
                val response: HttpResponse = httpClient.post("$baseURL/$method") {
                    parameter(currency, count)
                    parameter("user_id", userId)
                    parameter("comment", comment)
                    parameter("without_donate_score", withoutDonateScore)
                }

                if (response.status == HttpStatusCode.OK) {
                    val jsonResult = response.bodyAsText()
                    json.decodeFromString<ResponseResult>(jsonResult)
                } else {
                    ResponseResult(result = false, error = APIError(
                        code = response.status.toString().toInt(), description = response.bodyAsText())
                    )
                }

            } catch (e: IrisResponseException) {
                logger.error { "Ошибка при попытке передать валюту $e" }
                null
            }
        }
    }


    private suspend fun getBalance(method: String) : Any? {
        return withContext(Dispatchers.IO) {
            try {
                val response: HttpResponse = httpClient.get("$baseURL/$method")

                if (response.status == HttpStatusCode.OK) {
                    val jsonResult = response.bodyAsText()
                    json.decodeFromString<BalanceData>(jsonResult)
                } else {
                    ResponseResult(result = false, error = APIError(
                        code = response.status.toString().toInt(), description = response.bodyAsText())
                    )
                }

            } catch (e: IrisResponseException) {
                logger.error { "Ошибка при получение баланса бота $e" }
                null
            }
        }
    }


    private suspend fun getHistory(offset: Int, method: String): Any? {
        return withContext(Dispatchers.IO) {
            try {
                val response: HttpResponse = httpClient.get("$baseURL/$method") {
                    parameter("offset", offset)
                }

                if (response.status == HttpStatusCode.OK) {
                    val jsonResult = response.bodyAsText()
                    json.decodeFromString<List<HistoryData>>(jsonResult)
                } else {
                    ResponseResult(result = false, error = APIError(
                        code = response.status.toString().toInt(), description = response.bodyAsText())
                    )
                }

            } catch (e: IrisResponseException) {
                logger.error { "Ошибка при получение истории обмена валют $e" }
                null
            }
        }
    }
}
