package com.mieszko.employeesmanager.data.source.remote.api

import com.mieszko.employeesmanager.data.model.response.EmployeeResponse
import com.mieszko.employeesmanager.data.source.remote.ApiHeaders
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Query


interface EmployeeApi {

    @GET("employees")
    fun getEmployees(
        @HeaderMap headers: ApiHeaders.Authenticated,
        @Query("offset") offset: Int? = null,
        @Query("limit") limit: Int? = null
    ): Single<EmployeeResponse>

}