package com.mieszko.employeesmanager.application.di

import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import org.koin.dsl.module

val pagingConfigDependency = module {
    single {
        PagingConfig(
            pageSize = 10,
            initialLoadSize = 15,
            prefetchDistance = 10,
            enablePlaceholders = false,
            maxSize = PagingConfig.MAX_SIZE_UNBOUNDED,
            jumpThreshold = PagingSource.LoadResult.Page.COUNT_UNDEFINED
        )
    }
}