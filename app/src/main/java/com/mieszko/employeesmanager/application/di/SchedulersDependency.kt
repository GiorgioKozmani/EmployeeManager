package com.mieszko.employeesmanager.application.di

import com.mieszko.employeesmanager.domain.util.AppSchedulerProvider
import com.mieszko.employeesmanager.domain.util.SchedulerProvider
import org.koin.dsl.module

val schedulersDependency = module {
    single<SchedulerProvider> {
        AppSchedulerProvider()
    }
}

