package com.mieszko.employeesmanager.data.source.remote.api

import com.mieszko.employeesmanager.data.source.remote.ApiHeaders
import com.mieszko.employeesmanager.data.model.dto.AccessTokenDTO
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.HeaderMap
import retrofit2.http.POST

interface AccessTokenApi {

    enum class TempAppKeys(val value: String) {
        APP_CLIENT_ID("d2cc153a-3ad4-42b0-b832-43ee335e5ea5"),
        APP_REFRESH_TOKEN("Cd_XvxL-ukaX4wFejL1rjQ")
    }

    @POST("token")
    fun getAccessToken(
        @HeaderMap publicHeaders: ApiHeaders.ForAuthToken,
        @Body body: String = "client_id=${TempAppKeys.APP_CLIENT_ID.value}&grant_type=refresh_token&refresh_token=${TempAppKeys.APP_REFRESH_TOKEN.value}"
    ): Single<AccessTokenDTO>
}