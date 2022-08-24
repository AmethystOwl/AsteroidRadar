package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import kotlinx.coroutines.flow.Flow

@Dao
interface AsteroidDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg asteroids: Asteroid)

    @Query("DELETE FROM Asteroid WHERE closeApproachDate = :date")
    fun deleteAsteroidByDate(date: String)

    @Query("SELECT * FROM Asteroid ORDER BY closeApproachDate ASC")
    fun getAllAsteroids(): Flow<List<Asteroid>?>

    @Query("SELECT * FROM Asteroid WHERE closeApproachDate >= :startDate and closeApproachDate <= :endDate order by closeApproachDate asc")
    fun getAsteroidsOfRange(startDate: String, endDate: String): Flow<List<Asteroid>?>
}



@Dao
interface PictureOfTheDayDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg asteroids: PictureOfDay)

    @Query("SELECT * FROM PictureOfDay ORDER BY date ASC")
    fun getApod(): LiveData<PictureOfDay?>

    @Query("DELETE FROM PictureOfDay")
    fun deleteAll()

}