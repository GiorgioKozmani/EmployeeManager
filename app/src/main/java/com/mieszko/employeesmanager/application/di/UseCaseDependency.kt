package com.mieszko.employeesmanager.application.di

import com.mieszko.employeesmanager.domain.usecase.*
import org.koin.dsl.module

val useCaseDependency = module {
    factory<GetAccessTokenUseCase> {
        GetAccessTokenUseCaseImpl(
            authRepository = get(),
            schedulerProvider = get()
        )
    }
    factory<GetEmployeeProfileUseCase> {
        GetEmployeeProfileUseCaseImpl(
            employeeProfileRepository = get(),
            getAccessTokenUseCase = get(),
            schedulerProvider = get()
        )
    }
    factory<UpdateEmployeeProfileUseCase> {
        UpdateEmployeeProfileUseCaseImpl(
            employeeProfileRepository = get(),
            getAccessTokenUseCase = get(),
            schedulerProvider = get()
        )
    }
    factory<GetDepartmentUseCase> {
        GetDepartmentUseCaseImpl(
            departmentRepository = get(),
            getAccessTokenUseCase = get(),
            schedulerProvider = get()
        )
    }
    factory<GetEmployeesPagingSourceUseCase> {
        GetEmployeesPagingSourceUseCaseImpl(
            employeeRepository = get(),
            getAccessTokenUseCase = get(),
            getDepartmentUseCase = get(),
            schedulerProvider = get()
        )
    }
}