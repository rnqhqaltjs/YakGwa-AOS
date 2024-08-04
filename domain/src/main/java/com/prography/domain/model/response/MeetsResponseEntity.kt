package com.prography.domain.model.response

data class MeetsResponseEntity(
    val meetStatus: String,
    val meetInfo: MeetInfo,
) {
    data class MeetInfo(
        val meetThemeName: String,
        val meetDateTime: String?,
        val placeName: String?,
        val meetTitle: String?,
        val meetId: Int,
        val description: String?
    )
}