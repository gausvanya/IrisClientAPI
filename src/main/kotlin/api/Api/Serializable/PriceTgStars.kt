package IrisClientAPI.Api.Seralizable

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames


@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class PriceTgStarsSerialization(
    @JsonNames("result")
    val price: ResultPriceTgStars
)


@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class ResultPriceTgStars(
    @JsonNames("tgstars")
    val tgStars: Int,
    val sweets: Int
)
