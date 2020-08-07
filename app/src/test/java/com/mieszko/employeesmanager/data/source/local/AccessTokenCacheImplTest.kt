package com.mieszko.employeesmanager.data.source.local

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mieszko.employeesmanager.domain.model.AccessToken
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.verify


class AccessTokenCacheImplTest {
    private val testTokenJson: String = "test_token_json"
    private val gsonType = object : TypeToken<AccessToken>() {}.type

    private lateinit var sharedPrefsMock: SharedPrefs
    private lateinit var gsonMock: Gson
    private lateinit var tokenMock: AccessToken

    @Before
    fun setUp() {
        sharedPrefsMock = Mockito.mock(SharedPrefs::class.java)
        gsonMock = Mockito.mock(Gson::class.java)
        tokenMock = Mockito.mock(AccessToken::class.java)
    }

    @Test
    fun `cache token saves json to shared prefs`() {
        Mockito
            .`when`(gsonMock.toJson(tokenMock, gsonType))
            .thenReturn(testTokenJson)

        AccessTokenCacheImpl(sharedPrefsMock, gsonMock)
            .cacheAccessToken(tokenMock)

        verify(sharedPrefsMock, Mockito.times(1))
            .put(SharedPrefs.Keys.AccessToken, testTokenJson)
    }

    @Test
    fun `get token returns token when it's valid`() {
        mockValidToken(true)

        AccessTokenCacheImpl(sharedPrefsMock, gsonMock)
            .getAccessToken()
            .test()
            .assertValue(tokenMock)
    }

    @Test
    fun `get token throws exception when token is not valid`() {
        mockValidToken(false)

        AccessTokenCacheImpl(sharedPrefsMock, gsonMock)
            .getAccessToken()
            .test()
            .assertError { t ->
                t is SecurityException && t.message == "Access Token expired or not found."
            }
    }

    private fun mockValidToken(tokenIsValid: Boolean) {
        Mockito
            .`when`(sharedPrefsMock.getString(SharedPrefs.Keys.AccessToken))
            .thenReturn(testTokenJson)
        Mockito
            .`when`(gsonMock.fromJson<AccessToken?>(testTokenJson, gsonType))
            .thenReturn(tokenMock)
        Mockito
            .`when`(tokenMock.isValid())
            .thenReturn(tokenIsValid)
    }
}