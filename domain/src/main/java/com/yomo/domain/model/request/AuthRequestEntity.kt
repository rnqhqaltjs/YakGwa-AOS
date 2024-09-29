package com.yomo.domain.model.request

data class AuthRequestEntity(
    val loginType: String,
    val fcmToken: String
)