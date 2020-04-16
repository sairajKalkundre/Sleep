package com.example.sleep.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface SleepDatabaseDao {

    @Insert
    fun insert(night : SleepNight)

    @Update
    fun Update(night : SleepNight)

    @Query("SELECT * FROM daily_sleep_quality_table WHERE nightID = :key ")
    fun get(key : Long) : SleepNight

    @Query("DELETE FROM daily_sleep_quality_table")
    fun clear()

    @Query("SELECT * FROM daily_sleep_quality_table ORDER BY nightID DESC")
    fun getAllNights(): LiveData<List<SleepNight>>

    @Query("SELECT * FROM daily_sleep_quality_table ORDER BY nightID DESC LIMIT 1")
    fun getTonight(): SleepNight?
}