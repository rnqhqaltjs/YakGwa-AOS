package com.prography.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.prography.data.model.PromiseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PromiseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPromise(promiseEntity: PromiseEntity)

    @Delete
    suspend fun removePromise(promiseEntity: PromiseEntity)

    @Query("SELECT * FROM promise")
    fun getPromiseHistory(): Flow<List<PromiseEntity>>

    @Query("SELECT * FROM promise WHERE id = :id")
    fun getPromiseDetail(id: String): PromiseEntity
}