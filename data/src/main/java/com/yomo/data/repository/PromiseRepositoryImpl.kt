package com.yomo.data.repository

import com.yomo.data.local.PromiseDao
import com.yomo.data.mapper.PromiseMapper
import com.yomo.domain.model.Promise
import com.yomo.domain.repository.PromiseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PromiseRepositoryImpl @Inject constructor(
    private val promiseDao: PromiseDao
) : PromiseRepository {
    override suspend fun addPromise(promise: Promise) {
        val promiseEntity = PromiseMapper.mapperToPromiseEntity(promise)
        promiseDao.addPromise(promiseEntity)
    }

    override suspend fun removePromise(promise: Promise) {
        val promiseEntity = PromiseMapper.mapperToPromiseEntity(promise)
        promiseDao.removePromise(promiseEntity)
    }

    override fun getPromiseDetail(id: String): Promise {
        val promiseEntity = promiseDao.getPromiseDetail(id)
        return PromiseMapper.mapperToPromise(promiseEntity)
    }

    override fun getPromiseHistory(): Flow<List<Promise>> {
        return promiseDao.getPromiseHistory().map { promiseEntity ->
            promiseEntity.map {
                PromiseMapper.mapperToPromise(it)
            }
        }
    }
}
