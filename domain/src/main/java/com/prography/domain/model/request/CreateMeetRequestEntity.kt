package com.prography.domain.model.request

data class CreateMeetRequestEntity(
    val meetName: String,
    val meetDescription: String,
    val meetThemeId: Int,
    val placeDecide: Boolean,
    val places: List<String>,
    val timeDecide: Boolean,
    val voteDateRange: VoteDateRange,
    val voteTimeRange: VoteTimeRange
) {
    data class VoteDateRange(
        val start: String,
        val end: String
    )

    data class VoteTimeRange(
        val start: Start,
        val end: End
    )

    data class Start(
        val hour: Int,
        val minute: Int,
        val second: Int,
        val nano: Int
    )

    data class End(
        val hour: Int,
        val minute: Int,
        val second: Int,
        val nano: Int
    )
}