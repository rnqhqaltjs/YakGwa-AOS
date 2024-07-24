package com.prography.data.repository

import com.prography.data.local.PromiseDao
import com.prography.data.mapper.PromiseMapper
import com.prography.domain.model.Promise
import com.prography.domain.repository.PromiseRepository
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
