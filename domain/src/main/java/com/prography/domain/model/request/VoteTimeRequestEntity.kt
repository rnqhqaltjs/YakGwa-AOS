package com.prography.domain.model.request

data class VoteTimeRequestEntity(
    val enableTimes: List<EnableTimes>
) {
    data class EnableTimes(
        val enableTime: String
    )
}
