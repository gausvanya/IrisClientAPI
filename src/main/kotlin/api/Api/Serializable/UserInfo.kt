package IrisClientAPI.Api.Seralizable

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames


@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class UserRegInfoSerialization(
    @JsonNames("result")
    val reg: Long
)


@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class UserActivityInfoSerialization(
    @JsonNames("result")
    val activity: UserActivityResponse
)


@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class UserPocketInfoSerialization(
    @JsonNames("result")
    val pocket: UserPocketResponse
)


@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class UserSpamInfoSerialization(
    @JsonNames("result")
    val spam: UserSpamChecksResponse
)


@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class UserStarsInfoSerialization(
    @JsonNames("result")
    val checks: Int
)


@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class UserSpamChecksResponse(
    @JsonNames("is_spam")
    val isSpam: Boolean,
    @JsonNames("is_ignore")
    val isIgnore: Boolean,
    @JsonNames("is_scam")
    val isScam: Boolean
)


@Serializable
data class UserPocketResponse(
    val gold: Int,
    val coins: Int,
    val sweets: Double,
    val stars: Double
)


@Serializable
data class UserActivityResponse(
    val total: Int,
    val week: Int,
    val month: Int,
    val day: Int
)
