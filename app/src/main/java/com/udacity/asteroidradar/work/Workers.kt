package com.udacity.asteroidradar.work

import android.annotation.SuppressLint
import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.AsteroidsRepository
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfTheDayRepository
import com.udacity.asteroidradar.database.AsteroidsDatabase
import com.udacity.asteroidradar.database.PictureOfDayDatabase
import retrofit2.HttpException
import java.text.SimpleDateFormat
import java.util.*

class DeleteWorker(
    context: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {
    companion object {
        const val WORKER_NAME = "DeleteWorker"
    }

    @SuppressLint("NewApi")
    override suspend fun doWork(): Result {
        return try {
            val calendar = Calendar.getInstance()
            val currentTime = calendar.time
            val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
            val today = dateFormat.format(currentTime)

            val db = AsteroidsDatabase.getInstance(applicationContext)
            val repo = AsteroidsRepository(db)
            val todayAsteroids = repo.downloadAsteroidsOfToday(today)
            todayAsteroids?.let {
                val prevDay = dateFormat.format(calendar.add(Calendar.DAY_OF_YEAR, -1))
                db.dao.deleteAsteroidByDate(prevDay)
                Result.success()
            }
            Result.retry()
        } catch (httpException: HttpException) {
            Result.retry()
        }

    }
}

class RefreshAsteroidsWorker(context: Context, parameters: WorkerParameters) :
    CoroutineWorker(context, parameters) {
    companion object {
        const val WORKER_NAME = "RefreshAsteroidsWorker"
    }

    override suspend fun doWork(): Result {
        return try {
            val db = AsteroidsDatabase.getInstance(applicationContext)
            val repo = AsteroidsRepository(db)
            repo.refreshAsteroids()
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}




class RefreshApodWorker(context: Context, parameters: WorkerParameters) :
    CoroutineWorker(context, parameters) {
    companion object {
        const val WORKER_NAME = "RefreshApodWorker"
    }

    override suspend fun doWork(): Result {
        return try {
            val db = PictureOfDayDatabase.getInstance(applicationContext)
            val repo = PictureOfTheDayRepository(db)
            repo.downloadPictureOfTheDay()
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}