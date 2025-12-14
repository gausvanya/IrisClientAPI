package IrisClientAPI.Api.Seralizable

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames


@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class OrdersSerialization(
    @JsonNames("result")
    val orders: OrdersResponseResult
)


@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class OrdersResponseResult(
    @JsonNames("buy")
    val buys: List<OrderBuyTrade>? = null,
    @JsonNames("sell")
    val sells: List<OrderSellTrade>? = null
)
