package com.prography.data.model.response


import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseTimePlaceDto(
    @SerializedName("placeItems")
    val placeItems: List<PlaceItem>,
    @SerializedName("timeItems")
    val timeItems: TimeItems
) {
    @Serializable
    data class PlaceItem(
        @SerializedName("candidatePlaceId")
        val candidatePlaceId: Int,
        @SerializedName("name")
        val name: String,
        @SerializedName("address")
        val address: String,
        @SerializedName("description")
        val description: String,
    )

    @Serializable
    data class TimeItems(
        @SerializedName("timeRange")
        val timeRange: TimeRange,
        @SerializedName("dateRange")
        val dateRange: DateRange
    )

    @Serializable
    data class DateRange(
        @SerializedName("start")
        val start: String,
        @SerializedName("end")
        val end: String
    )

    @Serializable
    data class TimeRange(
        @SerializedName("start")
        val start: String,
        @SerializedName("end")
        val end: String
    )
}