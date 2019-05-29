package com.sdmadsen.steve.lawntracker

import androidx.lifecycle.LiveData
import androidx.room.*

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
    fun updateMow(vararg mow: Mow)

    @Delete
    fun deleteMow(vararg mow: Mow)
}