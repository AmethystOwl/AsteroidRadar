package com.udacity.asteroidradar

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Entity
@JsonClass(generateAdapter = true)
data class PictureOfDay(
    @PrimaryKey
    val url:String,
    val date:String,
    val explanation:String,
    val hdurl:String,
    @ColumnInfo(name = "media_type")
    @Json(name = "media_type")
    val mediaType:String,
    @Json(name = "service_version")
    @ColumnInfo(name = "service_version")
    val serviceVersion:String,
    val title:String
)