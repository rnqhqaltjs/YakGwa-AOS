package com.yomo.domain.model.response


data class ThemesResponseEntity(
    val themeId: Int,
    val themeName: String,
    var isSelected: Boolean = false
)