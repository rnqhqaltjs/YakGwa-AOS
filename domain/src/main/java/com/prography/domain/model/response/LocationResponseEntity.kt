package com.prography.domain.model.response

data class LocationResponseEntity(
    val title: String,
    val link: String,
    val category: String,
    val description: String,
    val telephone: String,
    val address: String,
    val roadAddress: String,
    val mapX: String,
    val mapY: String
)