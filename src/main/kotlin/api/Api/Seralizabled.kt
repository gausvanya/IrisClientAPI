package IrisClientAPI.Api

import kotlinx.serialization.ExperimentalSerializationApi
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


@OptIn(ExperimentalSerializationApi::class)
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


@Serializable
data class TradesOrderBookResponse(
    val volume: Int,
    val price: Double
)


@Serializable
data class TradesOrderBookTypesResponse(
    val buy: List<TradesOrderBookResponse>,
    val sell: List<TradesOrderBookResponse>
)


@Serializable
data class TradesDealsResponse(
    val id: Long,
    @JsonNames("group_id")
    val groupId: Long,
    val date: Double,
    val volume: Int,
    val type: String
)
