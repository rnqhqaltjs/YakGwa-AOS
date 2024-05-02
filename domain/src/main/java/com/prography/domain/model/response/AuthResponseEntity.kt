package com.prography.domain.model.response

data class AuthResponseEntity(
    val accessToken: String,
    val refreshToken: String,
    val isNew: Boolean
)