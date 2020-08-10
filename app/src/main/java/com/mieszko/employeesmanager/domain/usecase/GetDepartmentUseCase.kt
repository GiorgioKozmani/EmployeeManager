package com.mieszko.employeesmanager.domain.usecase

import com.mieszko.employeesmanager.domain.model.Department
import com.mieszko.employeesmanager.domain.repository.DepartmentRepository
import com.mieszko.employeesmanager.domain.util.SchedulerProvider
import io.reactivex.Single

interface GetDepartmentUseCase {
    operator fun invoke(id: Int): Single<Department>
}

class GetDepartmentUseCaseImpl(
    private val departmentRepository: DepartmentRepository,
    private val getAccessTokenUseCase: GetAccessTokenUseCase,
    private val schedulerProvider: SchedulerProvider
) :
    GetDepartmentUseCase {

    override operator fun invoke(id: Int): Single<Department> {
        return getAccessTokenUseCase()
            .flatMap { accessToken ->
                departmentRepository
                    .getDepartment(id, accessToken)
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
            }
    }
}