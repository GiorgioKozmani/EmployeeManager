package com.mieszko.employeesmanager.application.di

import com.google.gson.GsonBuilder
import com.mieszko.employeesmanager.data.source.local.SharedPrefs
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val sharedPrefsDependency = module {
    single {
        SharedPrefs(androidApplication())
    }
    single {
        GsonBuilder().create()
    }
}