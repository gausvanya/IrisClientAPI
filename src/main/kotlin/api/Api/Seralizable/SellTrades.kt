package IrisClientAPI.Api.Seralizable

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames


@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class SellTradesResponse(
    @JsonNames("done_volume")
    val doneVolume: Int,
    @JsonNames("sweets_received")
    val sweetsReceived: Double,
    @JsonNames("new_order")
    val newOrder: OrderSellTradesResponse? = null
)


@Serializable
data class OrderSellTradesResponse(
    val id: Int? = null,
    val volume: Int? = null,
    val price: Double? = null
)