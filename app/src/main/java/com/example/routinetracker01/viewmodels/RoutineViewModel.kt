package com.example.routinetracker01.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.routinetracker01.model.Routine

class RoutineViewModel : ViewModel() {
    private val routines = MutableLiveData<List<Routine>>()

    fun getRoutines(): LiveData<List<Routine>> = routines

    fun setRoutines(newRoutines: List<Routine>) {
        routines.value = newRoutines
    }
}
