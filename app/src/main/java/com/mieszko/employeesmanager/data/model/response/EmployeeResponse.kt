package com.mieszko.employeesmanager.data.model.response

import com.google.gson.annotations.SerializedName
import com.mieszko.employeesmanager.data.model.Paging
import com.mieszko.employeesmanager.data.model.dto.EmployeeDTO
import com.mieszko.employeesmanager.domain.model.Employee

data class EmployeeResponse(
    val paging: Paging,
    @SerializedName("data")
    val employeeDTOs: List<EmployeeDTO>
)