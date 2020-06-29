package com.mieszko.employeesmanager.data.source.remote.api

import com.mieszko.employeesmanager.data.model.response.DepartmentResponse
import com.mieszko.employeesmanager.data.source.remote.ApiHeaders
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Path


interface DepartmentApi {

    @GET("departments/{id}")
    fun getDepartment(
        @HeaderMap headers: ApiHeaders.Authenticated,
        @Path("id") id: Int
    ): Single<DepartmentResponse>

}