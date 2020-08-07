package com.mieszko.employeesmanager.application.di

import com.mieszko.employeesmanager.data.source.local.SharedPrefs
import com.mieszko.employeesmanager.data.source.local.SharedPrefsImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val sharedPrefsDependency = module {
    single<SharedPrefs> {
        SharedPrefsImpl(androidContext())
    }
}