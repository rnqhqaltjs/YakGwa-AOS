package com.prography.domain.model.request

data class CreateMeetRequestEntity(
    val meetName: String,
    val meetDescription: String,
    val meetThemeId: Int,
    val places: List<String>,
    val voteDateRange: VoteDateRange,
    val voteTimeRange: VoteTimeRange,
    val endVoteHour: Int
) {
    data class VoteDateRange(
        val start: String,
        val end: String
    )

    data class VoteTimeRange(
        val start: String,
        val end: String
    )
}