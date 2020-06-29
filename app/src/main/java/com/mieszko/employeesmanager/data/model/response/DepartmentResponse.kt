package com.mieszko.employeesmanager.data.model.response

import com.google.gson.annotations.SerializedName
import com.mieszko.employeesmanager.data.model.dto.DepartmentDTO

data class DepartmentResponse(
    @SerializedName("data")
    val departmentDTO: DepartmentDTO
)