package com.prography.domain.model.request

data class CreateMeetRequestEntity(
    val meetTitle: String,
    val description: String,
    val meetThemeId: Int,
    val confirmPlace: Boolean,
    val placeInfo: List<PlaceInfo>,
    val voteDate: VoteDate?,
    val meetTime: String?,
) {
    data class PlaceInfo(
        val title: String,
        val link: String,
        val category: String,
        val description: String,
        val telephone: String,
        val address: String,
        val roadAddress: String,
        val mapx: String,
        val mapy: String,
    )

    data class VoteDate(
        val startVoteDate: String,
        val endVoteDate: String
    )
}