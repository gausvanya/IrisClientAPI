package IrisClientAPI.Api.Seralizable

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames


@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class PriceTgStars(
    val result: ResultPriceTgStars
)


@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class ResultPriceTgStars(
    @JsonNames("tgstars")
    val tgstars: Int,
    val sweets: Int
)