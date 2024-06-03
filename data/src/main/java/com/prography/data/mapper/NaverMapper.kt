package com.prography.data.mapper

import com.prography.data.model.response.ResponseLocationDto
import com.prography.domain.model.response.LocationResponseEntity

object NaverMapper {
    fun mapperToLocationResponseEntity(responseLocationDto: ResponseLocationDto): List<LocationResponseEntity> {
        return responseLocationDto.items.map { item ->
            LocationResponseEntity(item.title)
        }
    }
}