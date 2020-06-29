package com.mieszko.employeesmanager.domain.usecase

import com.mieszko.employeesmanager.domain.model.Department
import com.mieszko.employeesmanager.domain.repository.DepartmentRepository
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

interface GetDepartmentUseCase {
    operator fun invoke(id: Int): Single<Department>
}

class GetDepartmentUseCaseImpl(
    private val departmentRepository: DepartmentRepository,
    private val getAccessTokenUseCase: GetAccessTokenUseCase
) :
    GetDepartmentUseCase {

    override operator fun invoke(id: Int): Single<Department> {
        return getAccessTokenUseCase()
            .flatMap { accessToken ->
                departmentRepository
                    .getDepartment(id, accessToken)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
            }
    }
}