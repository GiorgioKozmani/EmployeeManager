package com.mieszko.employeesmanager.application.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {

    private val _employeeUpdatedLiveData = MutableLiveData<Int>()

    val employeeUpdatedLiveData: LiveData<Int>
        get() = _employeeUpdatedLiveData

    fun employeeUpdated(id: Int) {
        _employeeUpdatedLiveData.value = id
    }
}
