package com.mieszko.employeesmanager.domain.model


sealed class Resource<out T> {
    data class Success<out T>(val data: T) : Resource<T>()
    data class Loading<out T>(val partialData: T? = null) : Resource<T>()
    data class Error<out T>(val throwable: Throwable? = null) : Resource<T>()
}