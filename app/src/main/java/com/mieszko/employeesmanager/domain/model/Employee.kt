package com.mieszko.employeesmanager.domain.model


data class Employee(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val departments: List<Department>
)