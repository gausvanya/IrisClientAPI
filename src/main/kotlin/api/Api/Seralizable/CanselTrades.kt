package IrisClientAPI.Api.Seralizable

import kotlinx.serialization.Serializable


@Serializable
data class CancelTradesResponse(
    val gold: Int? = null,
    val sweets: Double? = null
)