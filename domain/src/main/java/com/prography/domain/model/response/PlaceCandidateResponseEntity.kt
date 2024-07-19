package com.prography.domain.model.response

data class PlaceCandidateResponseEntity(
    val placeSlotId: Int,
    val placeName: String,
    val placeAddress: String,
    val userInfos: List<UserInfos>?,
) {
    data class UserInfos(
        val username: String,
        val imageUrl: String?,
    )
}