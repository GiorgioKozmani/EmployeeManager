package com.mieszko.employeesmanager.data.model

// In this project I decided not to use it as it does not my needs to when implementing JetPack Paging 3.0
// This would be changed in production app
data class Paging(
    val offset: Int?,
    val limit: Int?,
    val total: Int?
)