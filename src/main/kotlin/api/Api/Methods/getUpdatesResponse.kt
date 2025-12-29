package IrisClientAPI.Api.Methods

import IrisClientAPI.Api.IrisApiClient
import IrisClientAPI.Api.IrisResponseException
import IrisClientAPI.Api.Seralizable.UpdatesSerialization
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


internal suspend fun IrisApiClient.getUpdatesResponse(offset: Int, limit: Int, method: String): UpdatesSerialization? {
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
