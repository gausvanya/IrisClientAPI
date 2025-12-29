package IrisClientAPI.Api.Seralizable

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames


@Serializable
@OptIn(ExperimentalSerializationApi::class)
data class NFTInfoSerialization(
    @JsonNames("result")
    val info: NFTInfo
)


@Serializable
@OptIn(ExperimentalSerializationApi::class)
data class NFTInfo(
    val id: Int,
    @JsonNames("owner_id")
    val ownerId: Long,
    val name: String,
    @JsonNames("url_name")
    val urlName: String,
    val number: Int,
    val symbol: SymbolInfo,
    val background: BackgroundInfo,
    val model: ModelInfo
)


@Serializable
@OptIn(ExperimentalSerializationApi::class)
data class SymbolInfo(
    val id: Int,
    val name: String,
    val emoji: String,
    @JsonNames("custom_emoji_id")
    val customEmojiId: String,
    @JsonNames("rarity_per_mile")
    val rarityPerMile: Int
)


@Serializable
@OptIn(ExperimentalSerializationApi::class)
data class BackgroundInfo(
    val id: Int,
    val name: String,
    @JsonNames("rarity_per_mile")
    val rarityPerMile: Int
)


@Serializable
@OptIn(ExperimentalSerializationApi::class)
data class ModelInfo(
    val id: Int,
    val name: String,
    val emoji: String,
    @JsonNames("custom_emoji_id")
    val customEmojiId: String,
    @JsonNames("rarity_per_mile")
    val rarityPerMile: Int
)

