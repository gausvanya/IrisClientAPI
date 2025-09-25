package IrisClientAPI.Api.Seralizable

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames


@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class BalanceData(
    val gold: Int,
    val sweets: Double,
    @JsonNames("donate_score")
    val donateScore: Int,
)