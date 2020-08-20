package com.mieszko.employeesmanager.application.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mieszko.employeesmanager.domain.util.LiveDataEvent

class SharedViewModel : ViewModel() {

    private val _employeeUpdatedLiveData = MutableLiveData<LiveDataEvent<Int>>()

    val employeeUpdatedLiveData: LiveData<LiveDataEvent<Int>>
        get() = _employeeUpdatedLiveData

    fun employeeUpdated(id: Int) {
        _employeeUpdatedLiveData.value = LiveDataEvent(id)
    }
}
