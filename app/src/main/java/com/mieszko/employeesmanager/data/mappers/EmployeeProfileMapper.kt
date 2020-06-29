package com.mieszko.employeesmanager.data.mappers

import com.mieszko.employeesmanager.data.model.dto.EmployeeProfileDTO
import com.mieszko.employeesmanager.domain.model.EmployeeProfile
import com.mieszko.employeesmanager.domain.model.Gender

fun mapEmployeeProfileDto(employeeProfileDTO: EmployeeProfileDTO): EmployeeProfile {
    return EmployeeProfile(
        employeeProfileDTO.id,
        employeeProfileDTO.firstName.orEmpty(),
        employeeProfileDTO.lastName.orEmpty(),
        mapGenderString(employeeProfileDTO.gender)
    )
}

fun mapEmployeeProfile(employeeProfile: EmployeeProfile): EmployeeProfileDTO {
    return EmployeeProfileDTO(
        employeeProfile.id,
        employeeProfile.firstName,
        employeeProfile.lastName,
        mapGenderEnum(employeeProfile.gender)
    )
}

private fun mapGenderString(gender: String?): Gender? {
    return try {
        // In case gender string is null, or does not match any of "supported" :) genders thrown an exception
        Gender.valueOf(gender as String)
    } catch (e: Exception) {
        null
    }
}

private fun mapGenderEnum(gender: Gender?): String? {
    return gender?.name
}