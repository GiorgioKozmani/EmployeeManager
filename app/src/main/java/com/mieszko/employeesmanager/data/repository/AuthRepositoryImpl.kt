package com.mieszko.employeesmanager.data.repository

import com.mieszko.employeesmanager.data.mappers.mapAccessTokenDto
import com.mieszko.employeesmanager.data.source.local.AccessTokenCache
import com.mieszko.employeesmanager.data.source.remote.ApiHeadersProvider
import com.mieszko.employeesmanager.data.source.remote.api.AccessTokenApi
import com.mieszko.employeesmanager.domain.model.AccessToken
import com.mieszko.employeesmanager.domain.repository.AuthRepository
import io.reactivex.Single

class AuthRepositoryImpl(
    private val tokenApi: AccessTokenApi,
    private val headersProvider: ApiHeadersProvider,
    private val tokenCache: AccessTokenCache
) : AuthRepository {

    // First try to return token from cache, if not present (or valid) make an attempt to get it from api
    override fun getToken(): Single<AccessToken> {
        return tokenCache
            .getAccessToken()
            .onErrorResumeNext {
                tokenApi
                    .getAccessToken(headersProvider.getHeadersForAuthToken())
                    .map { mapAccessTokenDto(it) }
            }
    }

    override fun cacheToken(freshToken: AccessToken) {
        tokenCache.cacheAccessToken(freshToken)
    }

}