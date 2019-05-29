package com.sdmadsen.steve.lawntracker

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MowViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: MowRepository
    val allWords: LiveData<List<Mow>>

    init {
        val mowsDao = MowRoomDatabase.getDatabase(application, viewModelScope).mowDao()
        repository = MowRepository(mowsDao)
        allWords = repository.allMows
    }

    fun insert(mow: Mow) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(mow)
    }

    fun updateMows(mow: Mow) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateMows(mow)
    }

    fun deleteAll() = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteAll()
    }

    fun oneMow(refId: String) = runBlocking {
        repository.oneMow(refId)
    }

}