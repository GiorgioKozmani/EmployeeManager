package com.mieszko.employeesmanager.domain.repository

import com.mieszko.employeesmanager.domain.model.AccessToken
import com.mieszko.employeesmanager.domain.model.EmployeeProfile
import io.reactivex.Completable
import io.reactivex.Single

interface EmployeeProfileRepository {
    fun getEmployeeProfile(
        id: Int,
        accessToken: AccessToken
    ): Single<EmployeeProfile>

    fun updateEmployeeProfile(
        updatedEmployeeProfile: EmployeeProfile,
        accessToken: AccessToken
    ): Completable
}