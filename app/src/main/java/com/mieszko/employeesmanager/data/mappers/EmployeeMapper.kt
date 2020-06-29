package com.mieszko.employeesmanager.data.mappers

import com.mieszko.employeesmanager.data.model.dto.EmployeeDTO
import com.mieszko.employeesmanager.domain.model.Department
import com.mieszko.employeesmanager.domain.model.Employee

fun mapEmployeeDto(employeeDTO: EmployeeDTO, departments: List<Department>?): Employee {
    return Employee(
        employeeDTO.id,
        employeeDTO.firstName.orEmpty(),
        employeeDTO.lastName.orEmpty(),
        departments.orEmpty()
    )
}
