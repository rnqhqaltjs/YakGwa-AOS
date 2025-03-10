package com.yomo.domain.model.response

data class AuthResponseEntity(
    val accessToken: String,
    val refreshToken: String,
    val isNew: Boolean,
    val role: String?,
)