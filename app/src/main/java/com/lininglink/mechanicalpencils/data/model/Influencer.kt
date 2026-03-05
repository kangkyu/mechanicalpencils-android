package com.lininglink.mechanicalpencils.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Influencer(
    val id: Int,
    val name: String,
    val platform: String? = null,
    val handle: String? = null,
    @SerialName("avatar_url")
    val avatarUrl: String? = null,
    @SerialName("items_count")
    val itemsCount: Int = 0
)

@Serializable
data class InfluencerDetail(
    val id: Int,
    val name: String,
    val bio: String? = null,
    val platform: String? = null,
    val handle: String? = null,
    @SerialName("profile_url")
    val profileUrl: String? = null,
    @SerialName("follower_count")
    val followerCount: Int? = null,
    @SerialName("avatar_url")
    val avatarUrl: String? = null,
    val items: List<Item> = emptyList()
)

@Serializable
data class InfluencersResponse(
    val influencers: List<Influencer>
)

@Serializable
data class InfluencerDetailResponse(
    val influencer: InfluencerDetail
)
