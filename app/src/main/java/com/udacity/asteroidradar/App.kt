package com.udacity.asteroidradar

import android.app.Application
import androidx.work.*
import com.udacity.asteroidradar.work.DeleteWorker
import com.udacity.asteroidradar.work.RefreshApodWorker
import com.udacity.asteroidradar.work.RefreshAsteroidsWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.TimeUnit

class App : Application() {
    private val coroutine = CoroutineScope(Dispatchers.Default)
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        enqueueWorkers()
    }


    private fun enqueueWorkers() = coroutine.launch {
        val workManager = WorkManager.getInstance()
        val constraints = Constraints.Builder()
            .setRequiresCharging(true)
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val refreshAsteroidsPeriodicWorkPolicy = PeriodicWorkRequestBuilder<RefreshAsteroidsWorker>(
            1,
            TimeUnit.DAYS
        ).setConstraints(constraints).build()

        val deletePeriodicWorkPolicy = PeriodicWorkRequestBuilder<DeleteWorker>(
            1,
            TimeUnit.DAYS
        ).setConstraints(constraints).build()

        val refreshApodPeriodicWorkPolicy = PeriodicWorkRequestBuilder<RefreshApodWorker>(
            1,
            TimeUnit.DAYS
        ).setConstraints(constraints).build()

        workManager.enqueueUniquePeriodicWork(
            RefreshAsteroidsWorker.WORKER_NAME,
            ExistingPeriodicWorkPolicy.REPLACE,
            refreshAsteroidsPeriodicWorkPolicy
        )

        workManager.enqueueUniquePeriodicWork(
            DeleteWorker.WORKER_NAME,
            ExistingPeriodicWorkPolicy.REPLACE,
            deletePeriodicWorkPolicy
        )
        workManager.enqueueUniquePeriodicWork(
            RefreshApodWorker.WORKER_NAME,
            ExistingPeriodicWorkPolicy.REPLACE,
            refreshApodPeriodicWorkPolicy
        )

        Timber.d(
            workManager.getWorkInfoById(deletePeriodicWorkPolicy.id).get().state.toString()
        )
        Timber.d(
            workManager.getWorkInfoById(refreshAsteroidsPeriodicWorkPolicy.id).get().state.toString()
        )
        Timber.d(
            workManager.getWorkInfoById(refreshApodPeriodicWorkPolicy.id).get().state.toString()
        )
    }
}
