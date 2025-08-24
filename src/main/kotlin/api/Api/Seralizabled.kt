package IrisClientAPI.Api

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames


@Serializable
data class HistoryData(
    val date: Long,
    val amount: Double,
    val balance: Double,
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
    val commission: Double? = null
)


@Serializable
data class BalanceData(
    val gold: Double,
    val sweets: Double,
    @JsonNames("donate_score")
    val donateScore: Int,
)


@Serializable()
data class ResponseResult(
    val result: Boolean? = null,
    val error: APIError? = null
)


@Serializable
data class APIError(
    val code: Int,
    val description: String
)