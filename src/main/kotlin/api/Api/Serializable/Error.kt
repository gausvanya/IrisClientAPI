package IrisClientAPI.Api.Seralizable

import kotlinx.serialization.Serializable


@Serializable
data class ApiError(
    val code: Int,
    val description: String
)