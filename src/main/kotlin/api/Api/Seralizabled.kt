package IrisClientAPI.Api

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames


@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class HistoryData(
    val id: Int,
    val type: String,
    val date: Long,
    val amount: Double,
    val balance: Double,
    @JsonNames("peer_id")
    val peerId: Long,
    @JsonNames("to_user_id")
    val toUserId: Long,
    val details: DetailsHistoryData? = null,
    val comment: String? = null,
    // val metadata = null,
    )


@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class DetailsHistoryData(
    val total: Int? = null,
    val amount: Int? = null,
    @JsonNames("donate_score")
    val donateScore: Int? = null,
    val fee: Double? = null
    )


@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class BalanceData(
    val gold: Double,
    val sweets: Double,
    @JsonNames("donate_score")
    val donateScore: Int,
)


@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class UpdatesLog(
    val id: Int,
    val date: Long,
    val type: String,
    @JsonNames("object")
    val obj: HistoryData? = null
)


@Serializable
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


@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class TradesDealsResponse(
    val id: Long,
    @JsonNames("group_id")
    val groupId: Long,
    val date: Double,
    val volume: Int,
    val type: String
)
