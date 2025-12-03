package IrisClientAPI.Api.Seralizable

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames


@Serializable
@OptIn(ExperimentalSerializationApi::class)
data class Deal(
    val id: Int,
    @JsonNames("group_id")
    val groupId: Int? = null,
    val date: Long,
    val price: Double,
    val volume: Int,
    val type: String
)


@Serializable
data class DealsResponse(
    val result: List<Deal>
)