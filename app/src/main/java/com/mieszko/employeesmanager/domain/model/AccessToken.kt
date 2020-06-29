package com.mieszko.employeesmanager.domain.model

import java.util.*

data class AccessToken(
    val accessToken: String,
    private val expirationDate: Long
) {
    fun isValid(): Boolean {
        return Date().time < expirationDate
    }
}
