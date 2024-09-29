package com.yomo.domain.model.response

data class TimeCandidateResponseEntity(
    val meetStatus: String,
    val timeInfos: List<TimeInfo>?,
    val voteDate: VoteDate?
) {
    data class TimeInfo(
        val timeId: Int,
        val voteTime: String,
        var isSelected: Boolean = false
    )

    data class VoteDate(
        val startVoteDate: String,
        val endVoteDate: String,
    )

    fun selectFirstTime(): List<TimeInfo> = timeInfos?.mapIndexed { index, item ->
        item.copy(isSelected = index == 0)
    } ?: emptyList()
}