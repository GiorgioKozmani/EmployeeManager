package com.mieszko.employeesmanager.data.source.remote

import com.mieszko.employeesmanager.data.source.remote.api.AccessTokenApi
import com.mieszko.employeesmanager.domain.model.AccessToken

interface ApiHeadersProvider {
    fun getHeadersForAuthToken(): ApiHeaders.ForAuthToken
    fun getAuthenticatedHeaders(accessToken: AccessToken): ApiHeaders.Authenticated
}

class ApiHeadersProviderImpl :
    ApiHeadersProvider {

    override fun getHeadersForAuthToken(): ApiHeaders.ForAuthToken =
        ApiHeaders.ForAuthToken()
            .apply {
                put(CONTENT_TYPE, "application/x-www-form-urlencoded")
            }

    override fun getAuthenticatedHeaders(accessToken: AccessToken): ApiHeaders.Authenticated =
        ApiHeaders.Authenticated()
            .apply {
                put(CONTENT_TYPE, "application/json")
                put(AUTHORIZATION, getBearer(accessToken))
                put(CLIENT_ID, AccessTokenApi.TempAppKeys.APP_CLIENT_ID.value)
            }

    companion object {
        private const val CONTENT_TYPE = "Content-Type"
        private const val AUTHORIZATION = "Authorization"
        private const val CLIENT_ID = "X-ClientId"

        private fun getBearer(accessToken: AccessToken) = "Bearer ${accessToken.accessToken}"
    }
}

sealed class ApiHeaders : HashMap<String, String>() {
    class Authenticated : ApiHeaders()
    class ForAuthToken : ApiHeaders()
}
