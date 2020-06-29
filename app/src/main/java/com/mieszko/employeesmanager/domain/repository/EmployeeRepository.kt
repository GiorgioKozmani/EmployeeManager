package com.mieszko.employeesmanager.domain.repository

import com.mieszko.employeesmanager.domain.model.AccessToken
import com.mieszko.employeesmanager.domain.model.Employee
import com.mieszko.employeesmanager.domain.usecase.GetDepartmentUseCase
import io.reactivex.Single

interface EmployeeRepository {
    fun getEmployees(
        limit: Int?,
        offset: Int?,
        accessToken: AccessToken,
        getDepartmentUseCase: GetDepartmentUseCase
    ): Single<List<Employee>>
}