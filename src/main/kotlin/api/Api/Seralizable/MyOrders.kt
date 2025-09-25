package IrisClientAPI.Api.Seralizable

import kotlinx.serialization.Serializable


@Serializable
data class OrdersResponse(
    val buy: List<OrderBuyTradesResponse>? = null,
    val sell: List<OrderSellTradesResponse>? = null
)
