package com.yomo.domain.repository

import com.yomo.domain.model.Promise
import kotlinx.coroutines.flow.Flow

interface PromiseRepository {
    suspend fun addPromise(promise: Promise)
    suspend fun removePromise(promise: Promise)
    fun getPromiseDetail(id: String): Promise
    fun getPromiseHistory(): Flow<List<Promise>>
}