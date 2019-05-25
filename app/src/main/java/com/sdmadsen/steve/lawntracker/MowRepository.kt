package com.sdmadsen.steve.lawntracker

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData

class MowRepository(private val mowDao: MowDao) {

    val allMows: LiveData<List<Mow>> = mowDao.getAllMows()

    @WorkerThread
    suspend fun insert(mow: Mow) {
        mowDao.insert(mow)
    }

    @WorkerThread
    suspend fun deleteAll() {
        mowDao.deleteAll()
    }

    @WorkerThread
    suspend fun oneMow(refId: String): Mow {
        return mowDao.getOneMow(refId)
    }
}