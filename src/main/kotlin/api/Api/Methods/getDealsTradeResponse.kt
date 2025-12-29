package IrisClientAPI.Api.Methods

import IrisClientAPI.Api.IrisApiClient
import IrisClientAPI.Api.IrisResponseException
import IrisClientAPI.Api.Seralizable.ApiError
import IrisClientAPI.Api.Seralizable.DealsSerialization
import IrisClientAPI.Api.Seralizable.ResponseResult
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


internal suspend fun IrisApiClient.getDealsTradeResponse(id: Int, limit: Int, method: String): DealsSerialization? {
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
