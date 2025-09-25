package IrisClientAPI.Api.Seralizable

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
    val toUserId: Long? = null,
    val details: DetailsHistoryData? = null,
    val comment: String? = null,
    // val metadata = null - Планируется в будущем.
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