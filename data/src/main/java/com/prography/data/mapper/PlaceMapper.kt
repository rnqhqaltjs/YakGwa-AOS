package com.prography.data.mapper

import com.prography.data.model.request.RequestMyPlaceDto
import com.prography.data.model.response.ResponseLocationDto
import com.prography.domain.model.request.MyPlaceRequestEntity
import com.prography.domain.model.response.LocationResponseEntity

object PlaceMapper {
    fun mapperToLocationResponseEntity(responseLocationDto: List<ResponseLocationDto>): List<LocationResponseEntity> {
        return responseLocationDto.map { locationDto ->
            locationDto.run {
                LocationResponseEntity(
                    this.placeInfoDto.run {
                        LocationResponseEntity.PlaceInfoEntity(
                            this.title,
                            this.link,
                            this.category,
                            this.description,
                            this.telephone,
                            this.address,
                            this.roadAddress,
                            this.mapx,
                            this.mapy
                        )
                    },
                    this.isUserLike
                )
            }
        }
    }

    fun mapperToRequestMyPlaceDto(myPlaceRequestEntity: MyPlaceRequestEntity): RequestMyPlaceDto {
        return myPlaceRequestEntity.run {
            RequestMyPlaceDto(
                this.title,
                this.mapx,
                this.mapy
            )
        }
    }
}