package api.Api

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
    val commission: Double? = null
)


@Serializable
data class BalanceData(
    val gold: Double,
    val sweets: Double,
    @JsonNames("donate_score")
    val donateScore: Int,
)
