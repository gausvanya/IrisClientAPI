package IrisClientAPI.Api.Methods

import IrisClientAPI.Api.IrisApiClient
import IrisClientAPI.Api.IrisResponseException
import IrisClientAPI.Api.Seralizable.SellTradeSerialization
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


internal suspend fun IrisApiClient.sellTradeResponse(price: Double, volume: Int, method: String): SellTradeSerialization? {
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
