package IrisClientAPI.Api.Methods

import IrisClientAPI.Api.IrisApiClient
import IrisClientAPI.Api.IrisResponseException
import IrisClientAPI.Api.Seralizable.NFTListSerialization
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


internal suspend fun IrisApiClient.getNFTListResponse(limit: Int, offset: Int, method: String): NFTListSerialization? {
    return withContext(Dispatchers.IO) {
        try {
            val response: HttpResponse = httpClient.get("$baseURL/$method") {
                parameter("limit", limit)
                parameter("offset", offset)
            }

            if (response.status == HttpStatusCode.OK) {
                val jsonResult = response.bodyAsText()
                json.decodeFromString<NFTListSerialization>(jsonResult)
            } else throw IrisResponseException("${response.bodyAsText()} (${response.status.value})")

        } catch (e: IrisResponseException) {
            logger.error { "Ошибка при попытке получить список NFT: $e" }
            null
        }
    }
}
