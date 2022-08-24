package com.udacity.asteroidradar

import com.udacity.asteroidradar.api.ApodNetwork
import com.udacity.asteroidradar.api.AsteroidsNetwork
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidsDatabase
import com.udacity.asteroidradar.database.PictureOfDayDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import timber.log.Timber

class AsteroidsRepository(private val asteroidsDatabase: AsteroidsDatabase) {

    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            try {
                val response =
                    AsteroidsNetwork.asteroidsApi.getAsteroidsObjectAsync().await().string()
                val responseJsonObject = JSONObject(response)
                val asteroidList = parseAsteroidsJsonResult(responseJsonObject).toTypedArray()
                asteroidsDatabase.dao.insert(*asteroidList)
            } catch (e: Exception) {
                Timber.w("An error has occurred ${e.message}")
            }
        }
    }

    suspend fun downloadAsteroidsOfToday(today: String): Array<Asteroid>? {
        withContext(Dispatchers.IO) {
            try {
                val response = AsteroidsNetwork.asteroidsApi.getAsteroidsOfTodayObjectAsync(
                    startDate = today,
                    endDate = today
                ).await().string()
                val responseJsonObject = JSONObject(response)
                return@withContext parseAsteroidsJsonResult(responseJsonObject).toTypedArray()
            } catch (e: Exception) {
                Timber.w("An error has occurred ${e.message}")
            }

        }
        return null
    }
}

class PictureOfTheDayRepository(private val pictureOfTheDayDatabase: PictureOfDayDatabase) {

    suspend fun downloadPictureOfTheDay() {
        withContext(Dispatchers.IO) {
            try {
                val pic = ApodNetwork.apodApi.getPictureOfTheDayAsync().await()
                pictureOfTheDayDatabase.dao.insert(pic)
            } catch (e: Exception) {
                Timber.w("An error has occurred ${e.message}")
            }

        }

    }

}