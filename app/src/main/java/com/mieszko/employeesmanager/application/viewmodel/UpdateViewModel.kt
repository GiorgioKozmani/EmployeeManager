package com.mieszko.employeesmanager.application.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mieszko.employeesmanager.domain.model.EmployeeProfile
import com.mieszko.employeesmanager.domain.model.Gender
import com.mieszko.employeesmanager.domain.model.Resource
import com.mieszko.employeesmanager.domain.usecase.GetEmployeeProfileUseCase
import com.mieszko.employeesmanager.domain.usecase.UpdateEmployeeProfileUseCase
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.PublishSubject

class UpdateViewModel(
    private val employeeId: Int,
    getEmployeeProfileUseCase: GetEmployeeProfileUseCase,
    private val updateEmployeeProfileUseCase: UpdateEmployeeProfileUseCase
) : ViewModel() {
    private val disposablesBag = CompositeDisposable()

    val inputChange: PublishSubject<ProfileFieldChange> = PublishSubject.create()

    private var firstNameInput: String = ""
    private var lastNameInput: String = ""
    private var genderInput: Gender? = null

    private val _employeeProfileLiveData = MutableLiveData<Resource<EmployeeProfile>>()
    private val _updateStateLiveData = MutableLiveData<Resource<Int>>()

    val employeeProfileLiveData: LiveData<Resource<EmployeeProfile>> get() = _employeeProfileLiveData
    val updateStateLiveData: LiveData<Resource<Int>> get() = _updateStateLiveData

    init {
        disposablesBag
            .add(getEmployeeProfileUseCase(employeeId)
                .doOnSubscribe { _employeeProfileLiveData.value = Resource.Loading(null) }
                .subscribeBy(
                    onSuccess = { _employeeProfileLiveData.value = Resource.Success(it) },
                    onError = { _employeeProfileLiveData.value = Resource.Error(it) }
                ))

        disposablesBag.add(
            inputChange
                .subscribe { fieldChange ->
                    when (fieldChange) {
                        is ProfileFieldChange.FirstNameChange -> {
                            firstNameInput = fieldChange.firstName
                        }
                        is ProfileFieldChange.LastNameChange -> {
                            lastNameInput = fieldChange.lastName
                        }
                        is ProfileFieldChange.GenderChange -> {
                            genderInput = fieldChange.gender
                        }
                    }
                }
        )
    }

    fun onButtonClick() {
        disposablesBag
            .add(updateEmployeeProfileUseCase(
                EmployeeProfile(
                    employeeId,
                    firstNameInput,
                    lastNameInput,
                    genderInput
                )
            )
                .doOnSubscribe {
                    _updateStateLiveData.value = Resource.Loading(null)
                }
                .subscribeBy(
                    onComplete = { _updateStateLiveData.value = Resource.Success(employeeId) },
                    onError = { _updateStateLiveData.value = Resource.Error(null) }
                ))
    }

    override fun onCleared() {
        disposablesBag.clear()
        super.onCleared()
    }
}

sealed class ProfileFieldChange {
    class FirstNameChange(val firstName: String) : ProfileFieldChange()
    class LastNameChange(val lastName: String) : ProfileFieldChange()
    class GenderChange(val gender: Gender?) : ProfileFieldChange()
}