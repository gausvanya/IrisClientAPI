package IrisClientAPI.Api.Seralizable

import IrisClientAPI.Api.HistoryTypes
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames


@Serializable
@OptIn(ExperimentalSerializationApi::class)
data class NFTHistorySerialization(
    @JsonNames("result")
    val history: List<NFTHistory>
)


@Serializable
@OptIn(ExperimentalSerializationApi::class)
data class NFTHistory(
    val id: Int,
    val date: Long,
    val type: HistoryTypes,
    @JsonNames("nft_id")
    val nftId: Int,
    @JsonNames("peer_id")
    val peerId: Long,
    val comment: String? = null
)