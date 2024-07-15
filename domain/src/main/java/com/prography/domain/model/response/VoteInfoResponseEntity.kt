package com.prography.domain.model.response

data class VoteInfoResponseEntity(
    val voteStatus: String,
    val myPlaceVoteInfo: List<MyPlaceVoteInfo>,
    val myTimeVoteInfo: List<MyTimeVoteInfo>,
) {
    data class MyPlaceVoteInfo(
        val name: String,
        val address: String
    )

    data class MyTimeVoteInfo(
        val start: String,
        val end: String
    )
}