package com.yomo.domain.model.response

data class PlaceCandidateResponseEntity(
    val placeSlotId: Int,
    val placeName: String,
    val placeAddress: String,
    val userInfos: List<UserInfos>?,
    var isSelected: Boolean = false
) {
    data class UserInfos(
        val username: String,
        val imageUrl: String?,
    )
}