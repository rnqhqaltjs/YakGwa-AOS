package com.prography.domain.model.response

data class PromiseHistoryResponseEntity(
    val meetStatus: String,
    val meetInfo: MeetInfo,
    val description: String
) {
    data class MeetInfo(
        val meetThemeName: String,
        val meetDateTime: String?,
        val placeName: String?,
        val meetTitle: String,
        val meetId: Int
    )
}

