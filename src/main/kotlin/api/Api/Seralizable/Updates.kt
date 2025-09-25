package IrisClientAPI.Api.Seralizable

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames


@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class UpdatesLog(
    val id: Int,
    val date: Long,
    val type: String,
    @JsonNames("object")
    val obj: HistoryData? = null
)