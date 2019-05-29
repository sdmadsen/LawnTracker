package com.sdmadsen.steve.lawntracker

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TimeLogDao {

    @Query("SELECT * from time_log_table WHERE refId = :refId")
    fun getOneTimeLog(refId: String): TimeLog

    @Insert
    suspend fun insert(timeLog: TimeLog)

    @Query("DELETE FROM time_log_table")
    fun deleteAll()

    @Delete
    fun deleteTimeLog(vararg timeLog: TimeLog)

    @Query("SELECT * from time_log_table WHERE mowId = :mowId")
    fun getTimeElapsed(mowId: String): Int

}