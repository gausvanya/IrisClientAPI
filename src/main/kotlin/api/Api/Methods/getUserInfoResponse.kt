package IrisClientAPI.Api.Methods

import IrisClientAPI.Api.BotPermissions
import IrisClientAPI.Api.IrisApiClient
import IrisClientAPI.Api.IrisResponseException
import IrisClientAPI.Api.Seralizable.UserActivityInfoSerialization
import IrisClientAPI.Api.Seralizable.UserPocketInfoSerialization
import IrisClientAPI.Api.Seralizable.UserRegInfoSerialization
import IrisClientAPI.Api.Seralizable.UserSpamInfoSerialization
import IrisClientAPI.Api.Seralizable.UserStarsInfoSerialization
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


internal suspend fun <T> IrisApiClient.getUserInfoResponse(userId: Long, method: String): T? {
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
