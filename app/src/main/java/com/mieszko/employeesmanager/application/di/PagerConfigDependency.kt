package com.mieszko.employeesmanager.application.di

import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import org.koin.dsl.module

val pagingConfigDependency = module {
    single {
        PagingConfig(pageSize = 10)
    }
}