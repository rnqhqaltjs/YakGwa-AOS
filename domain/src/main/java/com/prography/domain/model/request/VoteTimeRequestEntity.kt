package com.prography.domain.model.request

data class VoteTimeRequestEntity(
    val possibleSchedules: List<PossibleSchedule>
) {
    data class PossibleSchedule(
        val possibleStartTime: String,
        val possibleEndTime: String
    )
}
