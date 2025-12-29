package IrisClientAPI.Api.Methods

import IrisClientAPI.Api.IrisApiClient
import IrisClientAPI.Api.IrisResponseException
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


internal suspend fun IrisApiClient.getIrisAgentsResponse(method: String): List<Long>? {
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
