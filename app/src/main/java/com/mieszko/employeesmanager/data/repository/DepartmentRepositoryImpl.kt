package com.mieszko.employeesmanager.data.repository

import com.mieszko.employeesmanager.data.mappers.mapDepartmentDto
import com.mieszko.employeesmanager.data.source.remote.ApiHeadersProvider
import com.mieszko.employeesmanager.data.source.remote.api.DepartmentApi
import com.mieszko.employeesmanager.domain.model.AccessToken
import com.mieszko.employeesmanager.domain.model.Department
import com.mieszko.employeesmanager.domain.repository.DepartmentRepository
import com.mieszko.employeesmanager.domain.usecase.GetAccessTokenUseCase
import io.reactivex.Single

class DepartmentRepositoryImpl(
    private val departmentApi: DepartmentApi,
    private val headersProvider: ApiHeadersProvider
) : DepartmentRepository {

    override fun getDepartment(id: Int, accessToken: AccessToken): Single<Department> {
        return departmentApi.getDepartment(
                    headers = headersProvider.getAuthenticatedHeaders(accessToken),
                    id = id
                )
            .map { responseDepartment -> mapDepartmentDto(responseDepartment.departmentDTO) }
    }

}