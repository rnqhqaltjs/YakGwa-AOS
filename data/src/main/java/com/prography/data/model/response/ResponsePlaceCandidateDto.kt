package com.prography.data.model.response


import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class ResponsePlaceCandidateDto(
    @SerializedName("placeSlotOfMeet")
    val placeSlotOfMeet: List<PlaceSlotOfMeet>
) {
    @Serializable
    data class PlaceSlotOfMeet(
        @SerializedName("placeSlotId")
        val placeSlotId: Int,
        @SerializedName("placeName")
        val placeName: String,
        @SerializedName("placeAddress")
        val placeAddress: String,
        @SerializedName("userInfos")
        val userInfos: List<UserInfos>?,
    )

    @Serializable
    data class UserInfos(
        @SerializedName("username")
        val username: String,
        @SerializedName("imageUrl")
        val imageUrl: String?,
    )
}