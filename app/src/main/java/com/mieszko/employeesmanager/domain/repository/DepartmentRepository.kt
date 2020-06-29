package com.mieszko.employeesmanager.domain.repository

import com.mieszko.employeesmanager.domain.model.AccessToken
import com.mieszko.employeesmanager.domain.model.Department
import io.reactivex.Single

interface DepartmentRepository {
    fun getDepartment(
        id: Int,
        accessToken: AccessToken
    ): Single<Department>
}