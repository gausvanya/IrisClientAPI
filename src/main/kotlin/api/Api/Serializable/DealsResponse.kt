package IrisClientAPI.Api.Seralizable

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames


@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class DealsSerialization(
    @JsonNames("result")
    val deals: List<DealResult>
)


@Serializable
@OptIn(ExperimentalSerializationApi::class)
data class DealResult(
    val id: Int,
    @JsonNames("group_id")
    val groupId: Int? = null,
    val date: Long,
    val price: Double,
    val volume: Int,
    val type: String
)
