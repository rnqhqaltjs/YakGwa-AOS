package com.yomo.domain.model.request

data class VoteTimeRequestEntity(
    val enableTimes: List<EnableTimes>
) {
    data class EnableTimes(
        val enableTime: String
    )
}
