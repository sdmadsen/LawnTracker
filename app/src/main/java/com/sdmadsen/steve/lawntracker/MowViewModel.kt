package com.sdmadsen.steve.lawntracker

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MowViewModel(application: Application) : AndroidViewModel(application) {

    private val mowRepository: MowRepository
    private val timeLogRepository: TimeLogRepository
    val allWords: LiveData<List<Mow>>

    init {
        val mowDao = MowRoomDatabase.getDatabase(application, viewModelScope).mowDao()
        mowRepository = MowRepository(mowDao)
        val timeLogDao = MowRoomDatabase.getDatabase(application, viewModelScope).timeLogDao()
        timeLogRepository = TimeLogRepository(timeLogDao)
        allWords = mowRepository.allMows
    }

    fun insertMow(mow: Mow) = viewModelScope.launch(Dispatchers.IO) {
        mowRepository.insert(mow)
    }
    fun updateMow(mow: Mow) = viewModelScope.launch(Dispatchers.IO) {
        mowRepository.updateMow(mow)
    }
    fun deleteMow(mow: Mow) = viewModelScope.launch(Dispatchers.IO) {
        mowRepository.deleteMow(mow)
    }
    fun deleteAllMows() = viewModelScope.launch(Dispatchers.IO) {
        mowRepository.deleteAll()
    }
    fun oneMow(refId: String) = runBlocking {
        mowRepository.oneMow(refId)
    }


    fun insertTimeLog(timeLog: TimeLog) = viewModelScope.launch(Dispatchers.IO) {
        timeLogRepository.insert(timeLog)
    }
}