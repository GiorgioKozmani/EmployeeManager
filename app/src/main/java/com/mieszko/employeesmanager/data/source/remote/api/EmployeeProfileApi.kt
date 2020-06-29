package com.mieszko.employeesmanager.data.source.remote.api

import com.mieszko.employeesmanager.data.model.dto.EmployeeProfileDTO
import com.mieszko.employeesmanager.data.model.response.EmployeeProfileResponse
import com.mieszko.employeesmanager.data.source.remote.ApiHeaders
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.*


interface EmployeeProfileApi {

    @GET("employees/{id}")
    fun getEmployeeProfile(
        @HeaderMap headers: ApiHeaders.Authenticated,
        @Path("id") id: Int
    ): Single<EmployeeProfileResponse>

    @PUT("employees/{id}")
    fun updateEmployeeProfile(
        @HeaderMap headers: ApiHeaders.Authenticated,
        @Path("id") id: Int,
        @Body body: EmployeeProfileDTO
    ): Completable
}