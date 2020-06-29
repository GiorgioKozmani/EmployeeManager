package com.mieszko.employeesmanager.data.repository

import com.mieszko.employeesmanager.data.mappers.mapEmployeeDto
import com.mieszko.employeesmanager.data.source.remote.ApiHeadersProvider
import com.mieszko.employeesmanager.data.source.remote.api.EmployeeApi
import com.mieszko.employeesmanager.domain.model.AccessToken
import com.mieszko.employeesmanager.domain.model.Employee
import com.mieszko.employeesmanager.domain.repository.EmployeeRepository
import com.mieszko.employeesmanager.domain.usecase.GetDepartmentUseCase
import io.reactivex.Single
import io.reactivex.rxkotlin.toObservable

class EmployeeRepositoryImpl(
    private val employeeApi: EmployeeApi,
    private val headersProvider: ApiHeadersProvider
) : EmployeeRepository {

    override fun getEmployees(
        limit: Int?,
        offset: Int?,
        accessToken: AccessToken,
        getDepartmentUseCase: GetDepartmentUseCase
    ): Single<List<Employee>> {
        return employeeApi.getEmployees(
            headers = headersProvider.getAuthenticatedHeaders(accessToken),
            offset = offset,
            limit = limit
        )
            .flatMapObservable { employeesResponse ->
                employeesResponse.employeeDTOs.toObservable()
            }
            .flatMapSingle { networkEmployee ->
                networkEmployee.departments
                    ?.toObservable()
                    ?.flatMapSingle { departmentId ->
                        getDepartmentUseCase(id = departmentId)
                    }
                    ?.toList()
                    ?.map { mapEmployeeDto(networkEmployee, it) }
            }.toList()
    }

}