package com.mieszko.employeesmanager.application.di

import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.mieszko.employeesmanager.data.source.local.SharedPrefs
import com.mieszko.employeesmanager.data.source.local.SharedPrefsImpl
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val sharedPrefsDependency = module {
    single<SharedPrefs> {
        SharedPrefsImpl(get())
    }
    single<SharedPreferences> {
        PreferenceManager.getDefaultSharedPreferences(androidApplication())
    }
}