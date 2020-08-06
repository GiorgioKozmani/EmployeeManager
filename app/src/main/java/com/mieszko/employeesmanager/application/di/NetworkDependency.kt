package com.mieszko.employeesmanager.application.di

import com.google.gson.GsonBuilder
import com.mieszko.employeesmanager.data.source.remote.ApiHeadersProvider
import com.mieszko.employeesmanager.data.source.remote.ApiHeadersProviderImpl
import com.mieszko.employeesmanager.data.source.remote.api.AccessTokenApi
import com.mieszko.employeesmanager.data.source.remote.api.DepartmentApi
import com.mieszko.employeesmanager.data.source.remote.api.EmployeeApi
import com.mieszko.employeesmanager.data.source.remote.api.EmployeeProfileApi
import com.readystatesoftware.chuck.ChuckInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

const val EMPLOYEE_API_URL_BASE = "https://openapi.planday.com/hr/v1/"
const val AUTH_API_URL_BASE = "https://id.planday.com/connect/"

val networkDependency = module {
    single {
        GsonBuilder().create()
    }
    single<ApiHeadersProvider> {
        ApiHeadersProviderImpl()
    }
    single {
        OkHttpClient
            .Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(get())
            .build()
    }
    single<Interceptor> {
        ChuckInterceptor(androidContext()).showNotification(false)
    }
    single<AccessTokenApi> {
        provideRetrofit(AUTH_API_URL_BASE, get()).create(
            AccessTokenApi::class.java
        )
    }

    single<EmployeeApi> {
        provideRetrofit(EMPLOYEE_API_URL_BASE, get()).create(
            EmployeeApi::class.java
        )
    }

    single<EmployeeProfileApi> {
        provideRetrofit(EMPLOYEE_API_URL_BASE, get()).create(
            EmployeeProfileApi::class.java
        )
    }

    single<DepartmentApi> {
        provideRetrofit(EMPLOYEE_API_URL_BASE, get()).create(
            DepartmentApi::class.java
        )
    }
}

fun provideRetrofit(baseUrl: String, okHttpClient: OkHttpClient): Retrofit =
    Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(baseUrl)
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()