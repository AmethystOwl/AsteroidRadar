package com.udacity.asteroidradar.api

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.model.PictureOfDay
import com.udacity.asteroidradar.utils.Constants
import kotlinx.coroutines.Deferred
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val apiKey = "YOUR_API_KEY"

interface AsteroidsApi {
    @GET("/neo/rest/v1/feed/")
    fun getAsteroidsObjectAsync(
        @Query("api_key") key: String = apiKey
    ): Deferred<ResponseBody>

    @GET("/neo/rest/v1/feed/")
    fun getAsteroidsOfTodayObjectAsync(
        @Query("api_key") key: String = apiKey,
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String
    ): Deferred<ResponseBody>
}


interface PictureOfTheDayApi {
    @GET("/planetary/apod/")
    fun getPictureOfTheDayAsync(
        @Query("api_key") key: String = apiKey
    ): Deferred<PictureOfDay>

}


object AsteroidsNetwork {
    private val retrofit = Retrofit.Builder().baseUrl(Constants.BASE_URL)
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

    val asteroidsApi: AsteroidsApi = retrofit.create(AsteroidsApi::class.java)
}

object ApodNetwork {
    private val retrofit = Retrofit.Builder().baseUrl(Constants.BASE_URL)
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .addConverterFactory(
            MoshiConverterFactory.create(
                Moshi.Builder()
                    .add(KotlinJsonAdapterFactory())
                    .build()
            )
        )
        .build()

    val apodApi: PictureOfTheDayApi = retrofit.create(PictureOfTheDayApi::class.java)
}
