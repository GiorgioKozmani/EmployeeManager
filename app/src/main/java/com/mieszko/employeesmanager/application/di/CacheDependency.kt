package com.mieszko.employeesmanager.application.di

import com.mieszko.employeesmanager.data.source.local.AccessTokenCache
import com.mieszko.employeesmanager.data.source.local.AccessTokenCacheImpl
import com.mieszko.employeesmanager.data.source.local.SharedPrefs
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val cacheDependency = module {
    single<AccessTokenCache> {
        AccessTokenCacheImpl(get(), get())
    }
}