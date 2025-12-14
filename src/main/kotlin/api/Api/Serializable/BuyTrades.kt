package IrisClientAPI.Api.Seralizable

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames


@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class BuyTradeSerialization(
    @JsonNames("result")
    val buyTrade: BuyTradeResult
)


@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class BuyTradeResult(
    @JsonNames("done_volume")
    val doneVolume: Int,
    @JsonNames("sweets_spent")
    val sweetsSpent: Double,
    @JsonNames("new_order")
    val newOrder: OrderBuyTrade? = null
)


@Serializable
data class OrderBuyTrade(
    val id: Int? = null,
    val volume: Int? = null,
    val price: Double? = null
)