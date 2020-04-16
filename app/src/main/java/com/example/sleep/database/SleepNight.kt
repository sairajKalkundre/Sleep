package com.example.sleep.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_sleep_quality_table")
data class SleepNight(
    @PrimaryKey(autoGenerate = true)
    var nightID : Long = 0L,

    @ColumnInfo(name = "start_time_milli")
    val startTimeMilli : Long = System.currentTimeMillis(),

    @ColumnInfo(name = "end_time_milli")
    var endTimeMilli : Long = startTimeMilli,

    @ColumnInfo(name = "quality_rating")
    val sleepQuality : Int = -1


)