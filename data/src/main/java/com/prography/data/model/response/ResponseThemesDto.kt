package com.prography.data.model.response


import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable


@Serializable
data class ResponseThemesDto(
    @SerializedName("meetThemeInfos")
    val meetThemeInfos: List<MeetThemeInfo>
) {
    @Serializable
    data class MeetThemeInfo(
        @SerializedName("meetThemeId")
        val meetThemeId: Int,
        @SerializedName("name")
        val name: String
    )
}

