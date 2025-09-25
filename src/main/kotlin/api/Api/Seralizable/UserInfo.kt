package IrisClientAPI.Api.Seralizable

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames


@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class UserRegInfoResponse(
    @JsonNames("result")
    val reg: Long
)


@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class UserActivityInfoResponse(
    @JsonNames("result")
    val activity: UserActivityResponse
)


@Serializable
data class UserActivityResponse(
    val total: Int,
    val week: Int,
    val month: Int,
    val day: Int
)


@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class UserSpamInfoResponse(
    @JsonNames("result")
    val spam: UserSpamChecksResponse
)


@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class UserStarsInfoResponse(
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


@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class UserPocketInfoResponse(
    @JsonNames("result")
    val pocket: UserPocketResponse
)


@Serializable
data class UserPocketResponse(
    val gold: Int,
    val coins: Int,
    val sweets: Double,
    val stars: Double
)