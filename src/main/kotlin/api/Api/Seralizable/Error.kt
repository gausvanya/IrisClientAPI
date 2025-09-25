package IrisClientAPI.Api.Seralizable

import kotlinx.serialization.Serializable


@Serializable
data class APIError(
    val code: Int,
    val description: String
)