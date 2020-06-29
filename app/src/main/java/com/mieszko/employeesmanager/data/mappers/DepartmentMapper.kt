package com.mieszko.employeesmanager.data.mappers

import com.mieszko.employeesmanager.data.model.dto.DepartmentDTO
import com.mieszko.employeesmanager.domain.model.Department

fun mapDepartmentDto(departmentDTO: DepartmentDTO): Department {
    return Department(
        departmentDTO.id,
        departmentDTO.name.orEmpty(),
        departmentDTO.number.orEmpty()
    )
}