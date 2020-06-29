package com.mieszko.employeesmanager.data.model.response

import com.google.gson.annotations.SerializedName
import com.mieszko.employeesmanager.data.model.dto.EmployeeProfileDTO

data class EmployeeProfileResponse(
    @SerializedName("data")
    val employeeProfileDTO: EmployeeProfileDTO
)