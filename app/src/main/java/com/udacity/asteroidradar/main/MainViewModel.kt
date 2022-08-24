package com.udacity.asteroidradar.main

import android.annotation.SuppressLint
import androidx.lifecycle.*
import com.udacity.asteroidradar.*
import com.udacity.asteroidradar.database.AsteroidsDatabase
import com.udacity.asteroidradar.database.PictureOfDayDatabase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class MainViewModel(
    private val asteroidsDatabase: AsteroidsDatabase,
    private val apodDatabase: PictureOfDayDatabase
) : ViewModel() {
    private var _asteroidsFiltered = MutableLiveData<List<Asteroid>?>()
    val asteroidsFiltered: LiveData<List<Asteroid>?> get() = _asteroidsFiltered

    val apod: LiveData<PictureOfDay?> = Transformations.map(apodDatabase.dao.getApod()){it}


    init {
        refreshAsteroids()
        getAsteroidsOfTheWeek()
        refreshApod()
    }

    private fun refreshAsteroids() {
        viewModelScope.launch {
            AsteroidsRepository(asteroidsDatabase).refreshAsteroids()
        }
    }

    private fun getAsteroidsInRange(startDate: String, endDate: String) {
        viewModelScope.launch {
            asteroidsDatabase.dao.getAsteroidsOfRange(startDate, endDate).collect {
                _asteroidsFiltered.value = it
            }
        }
    }

    @SuppressLint("NewApi")
    fun getAsteroidsOfToday() {
        val calendar = Calendar.getInstance()
        val currentTime = calendar.time
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        val today = dateFormat.format(currentTime)
        getAsteroidsInRange(today, today)
    }

    @SuppressLint("NewApi")
    fun getAsteroidsOfTheWeek() {
        val calendar = Calendar.getInstance()
        var currentTime = calendar.time
        var dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        val today = dateFormat.format(currentTime)
        calendar.add(Calendar.DAY_OF_YEAR, 6)
        currentTime = calendar.time
        dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        val lastDay = dateFormat.format(currentTime)
        getAsteroidsInRange(today, lastDay)
    }

    fun getAllAsteroids() {
        viewModelScope.launch {
            asteroidsDatabase.dao.getAllAsteroids().collect {
                _asteroidsFiltered.value = it
            }
        }
    }


    private fun refreshApod() {
        viewModelScope.launch {
            PictureOfTheDayRepository(apodDatabase).downloadPictureOfTheDay()
        }
    }
}