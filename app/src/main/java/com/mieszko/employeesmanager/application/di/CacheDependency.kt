package com.mieszko.employeesmanager.application.di

import com.mieszko.employeesmanager.data.source.local.AccessTokenCache
import com.mieszko.employeesmanager.data.source.local.AccessTokenCacheImpl
import org.koin.dsl.module

val cacheDependency = module {
    single<AccessTokenCache> {
        AccessTokenCacheImpl(get(), get())
    }
}