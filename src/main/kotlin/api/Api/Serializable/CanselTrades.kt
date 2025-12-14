package IrisClientAPI.Api.Seralizable

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames


@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class CanselTradeSerialization(
    @JsonNames("result")
    val cansel: CancelTradesResult
)


@Serializable
data class CancelTradesResult(
    val gold: Int? = null,
    val sweets: Double? = null
)