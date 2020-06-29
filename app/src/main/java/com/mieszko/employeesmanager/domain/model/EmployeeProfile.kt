package com.mieszko.employeesmanager.domain.model

data class EmployeeProfile(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val gender: Gender?
)

enum class Gender {
    Male, Female
}