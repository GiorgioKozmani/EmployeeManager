package com.mieszko.employeesmanager.data.repository

import com.mieszko.employeesmanager.data.mappers.mapEmployeeProfile
import com.mieszko.employeesmanager.data.mappers.mapEmployeeProfileDto
import com.mieszko.employeesmanager.data.source.remote.ApiHeadersProvider
import com.mieszko.employeesmanager.data.source.remote.api.EmployeeProfileApi
import com.mieszko.employeesmanager.domain.model.AccessToken
import com.mieszko.employeesmanager.domain.model.EmployeeProfile
import com.mieszko.employeesmanager.domain.repository.EmployeeProfileRepository
import io.reactivex.Completable
import io.reactivex.Single

class EmployeeProfileRepositoryImpl(
    private val employeeProfileApi: EmployeeProfileApi,
    private val headersProvider: ApiHeadersProvider
) : EmployeeProfileRepository {

    override fun getEmployeeProfile(id: Int, accessToken: AccessToken): Single<EmployeeProfile> {
        return employeeProfileApi.getEmployeeProfile(
            headers = headersProvider.getAuthenticatedHeaders(accessToken),
            id = id
        ).map { mapEmployeeProfileDto(it.employeeProfileDTO) }
    }

    override fun updateEmployeeProfile(
        updatedEmployeeProfile: EmployeeProfile,
        accessToken: AccessToken
    ): Completable {
        return employeeProfileApi.updateEmployeeProfile(
            headers = headersProvider.getAuthenticatedHeaders(accessToken),
            id = updatedEmployeeProfile.id,
            body = mapEmployeeProfile(updatedEmployeeProfile)
        )
    }

}