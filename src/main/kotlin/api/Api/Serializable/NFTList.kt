package IrisClientAPI.Api.Seralizable

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames


@Serializable
@OptIn(ExperimentalSerializationApi::class)
data class NFTListSerialization(
    @JsonNames("result")
    val nfts: List<NftInfo>
)


@Serializable
@OptIn(ExperimentalSerializationApi::class)
data class NftInfo(
    val id: Int,
    @JsonNames("owner_id")
    val ownerId: Long,
    @JsonNames("date_add")
    val dateAdd: Long,
    val name: String,
    @JsonNames("url_name")
    val urlName: String,
    val number: Int,
    val symbol: SymbolInfo,
    val background: BackgroundInfo,
    val model: ModelInfo
)
