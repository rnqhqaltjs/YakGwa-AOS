package com.prography.domain.model.response

data class TimePlaceResponseEntity(
    val placeItems: List<PlaceItem>,
    val timeItems: TimeItems
) {
    data class PlaceItem(
        val candidatePlaceId: Int,
        val name: String,
        val address: String,
        val description: String,
    )

    data class TimeItems(
        val timeRange: TimeRange,
        val dateRange: DateRange
    )

    data class DateRange(
        val start: String,
        val end: String
    )

    data class TimeRange(
        val start: String,
        val end: String
    )
}