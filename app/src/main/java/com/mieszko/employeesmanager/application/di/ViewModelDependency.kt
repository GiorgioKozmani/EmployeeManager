package com.mieszko.employeesmanager.application.di

import com.mieszko.employeesmanager.application.viewmodel.ListViewModel
import com.mieszko.employeesmanager.application.viewmodel.SharedViewModel
import com.mieszko.employeesmanager.application.viewmodel.UpdateViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelDependency = module {
    viewModel {
        SharedViewModel()
    }
    viewModel {
        ListViewModel(pagingConfig = get())
    }
    viewModel { (employeeId: Int) ->
        UpdateViewModel(
            employeeId = employeeId,
            getEmployeeProfileUseCase = get(),
            updateEmployeeProfileUseCase = get()
        )
    }
}