package com.yomo.data.mapper

import com.yomo.data.model.PromiseEntity
import com.yomo.domain.model.Promise

object PromiseMapper {
    fun mapperToPromise(promiseEntity: PromiseEntity): Promise {
        return promiseEntity.run {
            Promise(
                this.id,
                this.title,
                this.description,
                this.theme,
                this.userInfo.map { userInfo ->
                    userInfo.run {
                        Promise.UserInfo(
                            this.userId,
                            this.userName,
                            this.profileImage
                        )
                    }
                },
                this.timeInfo.run {
                    Promise.TimeInfo(
                        this.date,
                        this.time
                    )
                },
                this.placeInfo.run {
                    Promise.PlaceInfo(
                        this.placeName,
                        this.address,
                        this.mapx,
                        this.mapy
                    )
                }
            )
        }
    }

    fun mapperToPromiseEntity(promise: Promise): PromiseEntity {
        return promise.run {
            PromiseEntity(
                this.id,
                this.title,
                this.description,
                this.theme,
                this.userInfo.map { userInfo ->
                    userInfo.run {
                        PromiseEntity.UserInfo(
                            this.userId,
                            this.userName,
                            this.profileImage
                        )
                    }
                },
                this.timeInfo.run {
                    PromiseEntity.TimeInfo(
                        this.date,
                        this.time
                    )
                },
                this.placeInfo.run {
                    PromiseEntity.PlaceInfo(
                        this.placeName,
                        this.address,
                        this.mapx,
                        this.mapy
                    )
                }
            )
        }
    }
}