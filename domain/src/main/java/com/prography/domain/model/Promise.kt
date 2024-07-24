package com.prography.domain.model

data class Promise(
    val id: Int,
    val title: String,
    val description: String,
    val theme: String,
    val userInfo: List<UserInfo>,
    val timeInfo: TimeInfo,
    val placeInfo: PlaceInfo,
) {
    data class UserInfo(
        val userId: Int,
        val userName: String,
        val profileImage: String,
    )

    data class TimeInfo(
        val date: String,
        val time: String
    )

    data class PlaceInfo(
        val placeName: String,
        val address: String,
        val mapx: String,
        val mapy: String
    )
}
