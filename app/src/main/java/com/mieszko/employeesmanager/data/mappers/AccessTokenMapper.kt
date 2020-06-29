package com.mieszko.employeesmanager.data.mappers

import com.mieszko.employeesmanager.data.model.dto.AccessTokenDTO
import com.mieszko.employeesmanager.domain.model.AccessToken
import java.util.*
import java.util.concurrent.TimeUnit

fun mapAccessTokenDto(accessTokenDto: AccessTokenDTO): AccessToken {
    return AccessToken(
        accessTokenDto.accessToken.orEmpty(),
        calculateExpirationDate(accessTokenDto.expiresIn)
    )
}

private fun calculateExpirationDate(expiresIn: Int?): Long {
    return if (expiresIn == null) {
        //in case we get null as expiresIn, we set expiration date to current date, meaning it's invalid from the beginning
        Date().time
    } else {
        //expiration date is set to current time plus tokenValidTime, shortened by 60 seconds
        //to make sure that network call and computation duration does not lead to unexpected behaviour
        Date().time + TimeUnit.MILLISECONDS.convert(expiresIn.toLong() - 60, TimeUnit.SECONDS)
    }
}