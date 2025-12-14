package IrisClientAPI.Api.Seralizable

import IrisClientAPI.Api.UpdateTypes
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames


@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class UpdatesSerialization(
    @JsonNames("result")
    val updates: List<UpdatesLog>
)


@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class UpdatesLog(
    val id: Int,
    val date: Long,
    val type: UpdateTypes,
    @JsonNames("object")
    val obj: HistoryDataResult? = null
)
