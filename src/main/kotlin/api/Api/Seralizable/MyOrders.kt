package IrisClientAPI.Api.Seralizable

import kotlinx.serialization.Serializable

@Serializable
data class OrdersResponse(
    val result: OrdersResponseResult
)

@Serializable
data class OrdersResponseResult(
    val buy: List<OrderBuyTradesResponse>? = null,
    val sell: List<OrderSellTradesResponse>? = null
)
