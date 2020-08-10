package com.mieszko.employeesmanager.domain.usecase

import com.mieszko.employeesmanager.domain.model.EmployeeProfile
import com.mieszko.employeesmanager.domain.repository.EmployeeProfileRepository
import com.mieszko.employeesmanager.domain.util.SchedulerProvider
import io.reactivex.Single

interface GetEmployeeProfileUseCase {
    operator fun invoke(id: Int): Single<EmployeeProfile>
}

class GetEmployeeProfileUseCaseImpl(
    private val employeeProfileRepository: EmployeeProfileRepository,
    private val getAccessTokenUseCase: GetAccessTokenUseCase,
    private val schedulerProvider: SchedulerProvider
) :
    GetEmployeeProfileUseCase {

    override operator fun invoke(id: Int): Single<EmployeeProfile> {
        return getAccessTokenUseCase()
            .flatMap { accessToken ->
                employeeProfileRepository
                    .getEmployeeProfile(id, accessToken)
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
            }

    }
}
