package com.prography.domain.model.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CreateMeetResponseEntity(
    val meetId: Int
) : Parcelable