package com.mieszko.employeesmanager.data.model.dto

import com.google.gson.annotations.SerializedName

data class AccessTokenDTO(
    @SerializedName("access_token")
    val accessToken: String?,
    @SerializedName("expires_in")
    val expiresIn: Int?
)
