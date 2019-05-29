package com.sdmadsen.steve.lawntracker

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData

class TimeLogRepository(private val timeLogDao: TimeLogDao) {

    @WorkerThread
    suspend fun insert(timeLog: TimeLog) {
        timeLogDao.insert(timeLog)
    }

    @WorkerThread
    fun deleteTimeLog(timeLog: TimeLog) {
        timeLogDao.deleteTimeLog(timeLog)
    }

    @WorkerThread
    fun deleteAll() {
        timeLogDao.deleteAll()
    }

    @WorkerThread
    fun oneTimeLog(refId: String): TimeLog {
        return timeLogDao.getOneTimeLog(refId)
    }
}