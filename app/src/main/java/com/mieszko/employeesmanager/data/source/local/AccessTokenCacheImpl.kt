package com.mieszko.employeesmanager.data.source.local

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mieszko.employeesmanager.domain.model.AccessToken
import io.reactivex.Single
import java.lang.reflect.Type

interface AccessTokenCache {
    fun getAccessToken(): Single<AccessToken>
    fun cacheAccessToken(newAccessToken: AccessToken)
}

class AccessTokenCacheImpl(
    private val sharedPrefs: SharedPrefs,
    private val gson: Gson
) : AccessTokenCache {
    private val type: Type = object : TypeToken<AccessToken>() {}.type

    override fun getAccessToken(): Single<AccessToken> {
        val savedJson = sharedPrefs.getString(SharedPrefs.Keys.AccessToken)
        val savedToken: AccessToken? = gson.fromJson(savedJson, type)
        return if (savedToken != null && savedToken.isValid()) {
            Single.just(savedToken)
        } else {
            Single.error(Exception("Access Token expired or not found."))
        }
    }

    override fun cacheAccessToken(newAccessToken: AccessToken) {
        sharedPrefs.put(SharedPrefs.Keys.AccessToken, gson.toJson(newAccessToken, type))
    }
}