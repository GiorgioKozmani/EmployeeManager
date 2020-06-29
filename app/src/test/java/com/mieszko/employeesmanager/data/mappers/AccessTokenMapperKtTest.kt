package com.mieszko.employeesmanager.data.mappers

import com.mieszko.employeesmanager.data.model.dto.AccessTokenDTO
import org.junit.Assert
import org.junit.Test

class AccessTokenMapperKtTest {

    @Test
    fun `token invalidates immediately if expires in is null`() {
        val tokenDTO = AccessTokenDTO("test_token", null)
        val token = mapAccessTokenDto(tokenDTO)
        Assert.assertEquals(false, token.isValid())
    }
}