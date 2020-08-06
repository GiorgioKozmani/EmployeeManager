package com.mieszko.employeesmanager

import org.mockito.Mockito

object TestUtil {
    inline fun <reified T> anyNonNull(): T = Mockito.any<T>(T::class.java)
}