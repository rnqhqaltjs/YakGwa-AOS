package com.prography.yakgwa.model

import com.prography.domain.model.response.ThemesResponseEntity

data class ThemeModel(
    val themesResponseEntity: ThemesResponseEntity,
    var isSelected: Boolean = false
)