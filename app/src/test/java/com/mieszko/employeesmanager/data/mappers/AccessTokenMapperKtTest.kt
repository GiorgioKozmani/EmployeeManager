package com.mieszko.employeesmanager.data.mappers

import com.mieszko.employeesmanager.data.model.dto.AccessTokenDTO
import com.mieszko.employeesmanager.domain.model.AccessToken
import org.junit.Assert
import org.junit.Test

class AccessTokenMapperKtTest {

    @Test
    fun `token invalidates immediately if expires_in is null`() {
        Assert.assertEquals(false, getTestToken(null).isValid())
    }

    @Test
    fun `token is NOT valid if it's less than 60 seconds until it's expiration date`() {
        Assert.assertEquals(false, getTestToken(59).isValid())
    }

    @Test
    fun `token is valid if it's more than 60 seconds until it's expiration date`() {
        Assert.assertEquals(true, getTestToken(61).isValid())
    }

    private fun getTestToken(expiresIn: Int?): AccessToken {
        val tokenDTO = AccessTokenDTO("test_token", expiresIn)
        return mapAccessTokenDto(tokenDTO)
    }
}