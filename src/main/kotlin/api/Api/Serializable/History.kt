package IrisClientAPI.Api.Seralizable

import IrisClientAPI.Api.HistoryTypes
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames


@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class HistorySerialization(
    @JsonNames("result")
    val history: List<HistoryDataResult>
)


@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class HistoryDataResult(
    val id: Int,
    val type: HistoryTypes,
    val date: Long,
    val amount: Double,
    val balance: Double,
    @JsonNames("peer_id")
    val peerId: Long,
    @JsonNames("to_user_id")
    val toUserId: Long? = null,
    val details: DetailsHistoryData? = null,
    val comment: String? = null
)


@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class DetailsHistoryData(
    val total: Double? = null,
    val amount: Int? = null,
    @JsonNames("donate_score")
    val donateScore: Int? = null,
    val fee: Double? = null
)
