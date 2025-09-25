package IrisClientAPI.Api.Seralizable

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames


@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class BuyTradesResponse(
    @JsonNames("done_volume")
    val doneVolume: Int,
    @JsonNames("sweets_spent")
    val sweetsSpent: Double,
    @JsonNames("new_order")
    val newOrder: OrderBuyTradesResponse? = null
)


@Serializable
data class OrderBuyTradesResponse(
    val id: Int? = null,
    val volume: Int? = null,
    val price: Double? = null
)