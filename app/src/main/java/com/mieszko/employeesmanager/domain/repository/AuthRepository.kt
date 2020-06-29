package com.mieszko.employeesmanager.domain.repository

import com.mieszko.employeesmanager.domain.model.AccessToken
import io.reactivex.Single

interface AuthRepository {
    fun getToken(): Single<AccessToken>
    fun cacheToken(freshToken: AccessToken)
}