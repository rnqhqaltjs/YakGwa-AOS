package com.prography.domain.model.response

data class TimeCandidateResponseEntity(
    val meetStatus: String,
    val timeInfos: List<TimeInfo>?,
    val voteDate: VoteDate?
) {
    data class TimeInfo(
        val timeId: Int,
        val voteTime: String
    )

    data class VoteDate(
        val startVoteDate: String,
        val endVoteDate: String,
    )
}