package com.mieszko.employeesmanager.application.di

import com.google.gson.GsonBuilder
import org.koin.dsl.module

val gsonDependency = module {
    single {
        GsonBuilder().create()
    }
}

