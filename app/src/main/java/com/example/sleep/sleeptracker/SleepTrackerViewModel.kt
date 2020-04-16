package com.example.sleep.sleeptracker

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.android.trackmysleepquality.formatNights
import com.example.sleep.database.SleepDatabaseDao
import com.example.sleep.database.SleepNight
import kotlinx.coroutines.*

class SleepTrackerViewModel(val database : SleepDatabaseDao ,
                            application: Application) : AndroidViewModel(application) {

    private var viewModelJob = Job()

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var tonight = MutableLiveData<SleepNight?>()

    private var nights = database.getAllNights()

    val nightsString = Transformations.map(nights){nights ->
        formatNights(nights,application.resources)

    }

    init {
        intializeTonight()
    }
    private fun intializeTonight(){
        uiScope.launch {
            tonight.value = getTonightFromDatabase()
        }
    }

    private suspend fun getTonightFromDatabase(): SleepNight? {
        return withContext(Dispatchers.IO){
            var night = database.getTonight()
            if( night?.endTimeMilli != night?.startTimeMilli){
                night = null
            }
            night
        }
    }

    fun onStartTracking(){
        uiScope.launch {

            val newNight = SleepNight()

            insert(newNight)

            tonight.value = getTonightFromDatabase()
        }
    }

    private suspend fun insert(newNight: SleepNight) {
            withContext(Dispatchers.IO)
            {
                database.insert(newNight)
            }
    }

    fun onStropTracking(){
        uiScope.launch {
            val oldNight = tonight.value ?: return@launch

            oldNight.endTimeMilli = System.currentTimeMillis()

            update(oldNight)
        }
    }

    private suspend fun update(oldNight: SleepNight) {
            withContext(Dispatchers.IO){
                database.Update(oldNight)
            }
    }

    fun onClear(){
        uiScope.launch {
            clear()
            tonight.value = null
        }
    }

    suspend fun clear(){
        withContext(Dispatchers.IO){
            database.clear()
        }
    }
}