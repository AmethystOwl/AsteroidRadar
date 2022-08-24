package com.udacity.asteroidradar.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.udacity.asteroidradar.database.AsteroidsDatabase
import com.udacity.asteroidradar.database.PictureOfDayDatabase

@Suppress("UNCHECKED_CAST")
class MainViewModelFactory(private val asteroidsDatabase: AsteroidsDatabase,
                           private val apodDatabase:PictureOfDayDatabase) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MainViewModel::class.java)){
            return MainViewModel(asteroidsDatabase,apodDatabase) as T
        }
        throw IllegalArgumentException("ViewModel Not Found")
    }
}