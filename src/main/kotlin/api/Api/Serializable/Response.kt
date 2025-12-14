package IrisClientAPI.Api.Seralizable

import kotlinx.serialization.Serializable


@Serializable
data class ResponseResult(
    val result: Int? = null,
    val error: ApiError? = null
)