package com.mieszko.employeesmanager.application

import android.app.Application
import com.mieszko.employeesmanager.application.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

@Suppress("Unused")
class EmployeesManagerApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@EmployeesManagerApp)
            modules(
                listOf(
                    schedulersDependency,
                    gsonDependency,
                    cacheDependency,
                    networkDependency,
                    repositoryDependency,
                    sharedPrefsDependency,
                    useCaseDependency,
                    viewModelDependency,
                    pagingConfigDependency
                )
            )
        }
    }

}
