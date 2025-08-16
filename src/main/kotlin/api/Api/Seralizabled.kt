package api.Api

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNames


@Serializable
data class HistoryData(
    val date: Long,
    val amount: Float,
    val balance: Float,
    @JsonNames("to_user_id")
    val toUserId: Long,
    val id: Int,
    val type: String,
    val info: InfoData
)


@Serializable
data class InfoData(
    val donateScore: Int? = null,
    val sweets: Int? = null,
    val golds: Int? = null,
    val commission: Float? = null
)


@Serializable
data class BalanceData(
    val gold: Float,
    val sweets: Float,
    @JsonNames("donate_score")
    val donateScore: Int,
)


@Serializable()
data class ResponseResult(
    val result: JsonElement? = null,
    val error: APIError? = null
)


@Serializable
data class APIError(
    val code: Int? = null,
    val description: String? = null
)