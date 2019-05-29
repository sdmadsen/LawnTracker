package com.sdmadsen.steve.lawntracker

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface MowDao {

    @Query("SELECT * from mow_table ORDER BY date_created DESC")
    fun getAllMows(): LiveData<List<Mow>>

    @Query("SELECT * from mow_table WHERE refId = :refId")
    fun getOneMow(refId: String): Mow

    @Insert
    suspend fun insert(mow: Mow)

    @Query("DELETE FROM mow_table")
    fun deleteAll()

    @Update
    fun updateMows(vararg mow: Mow)
}