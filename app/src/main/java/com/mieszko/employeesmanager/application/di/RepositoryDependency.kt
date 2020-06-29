package com.mieszko.employeesmanager.application.di

import com.mieszko.employeesmanager.data.repository.AuthRepositoryImpl
import com.mieszko.employeesmanager.data.repository.DepartmentRepositoryImpl
import com.mieszko.employeesmanager.data.repository.EmployeeProfileRepositoryImpl
import com.mieszko.employeesmanager.data.repository.EmployeeRepositoryImpl
import com.mieszko.employeesmanager.domain.repository.AuthRepository
import com.mieszko.employeesmanager.domain.repository.DepartmentRepository
import com.mieszko.employeesmanager.domain.repository.EmployeeProfileRepository
import com.mieszko.employeesmanager.domain.repository.EmployeeRepository
import org.koin.dsl.module

val repositoryDependency = module {
    single<AuthRepository> {
        AuthRepositoryImpl(
            headersProvider = get(),
            tokenApi = get(),
            tokenCache = get()
        )
    }

    single<EmployeeRepository> {
        EmployeeRepositoryImpl(
            headersProvider = get(),
            employeeApi = get()
        )
    }

    single<EmployeeProfileRepository> {
        EmployeeProfileRepositoryImpl(
            headersProvider = get(),
            employeeProfileApi = get()
        )
    }

    single<DepartmentRepository> {
        DepartmentRepositoryImpl(
            headersProvider = get(),
            departmentApi = get()
        )
    }
}
