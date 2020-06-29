package com.mieszko.employeesmanager.data.model.dto


data class EmployeeDTO(
    val id: Int,
    val firstName: String?,
    val lastName: String?,
    val departments: List<Int>?
)




