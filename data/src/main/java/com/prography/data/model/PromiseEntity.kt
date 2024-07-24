package com.prography.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "promise")
data class PromiseEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val title: String,
    val description: String,
    val theme: String,
    val userInfo: List<UserInfo>,
    val timeInfo: TimeInfo,
    val placeInfo: PlaceInfo,
) {
    data class UserInfo(
        @ColumnInfo(name = "user_id")
        val userId: Int,
        @ColumnInfo(name = "user_name")
        val userName: String,
        @ColumnInfo(name = "profile_image")
        val profileImage: String,
    )

    data class TimeInfo(
        val date: String,
        val time: String
    )

    data class PlaceInfo(
        @ColumnInfo(name = "place_name")
        val placeName: String,
        val address: String,
        val mapx: String,
        val mapy: String
    )
}
