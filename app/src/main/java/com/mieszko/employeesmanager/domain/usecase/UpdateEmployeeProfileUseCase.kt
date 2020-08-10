package com.mieszko.employeesmanager.domain.usecase

import com.mieszko.employeesmanager.domain.model.EmployeeProfile
import com.mieszko.employeesmanager.domain.repository.EmployeeProfileRepository
import com.mieszko.employeesmanager.domain.util.SchedulerProvider
import io.reactivex.Completable

interface UpdateEmployeeProfileUseCase {
    operator fun invoke(updatedProfile: EmployeeProfile): Completable
}

class UpdateEmployeeProfileUseCaseImpl(
    private val employeeProfileRepository: EmployeeProfileRepository,
    private val getAccessTokenUseCase: GetAccessTokenUseCase,
    private val schedulerProvider: SchedulerProvider
) : UpdateEmployeeProfileUseCase {

    override operator fun invoke(
        updatedProfile: EmployeeProfile
    ): Completable {
        return getAccessTokenUseCase()
            .flatMapCompletable { accessToken ->
                employeeProfileRepository
                    .updateEmployeeProfile(updatedProfile, accessToken)
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
            }
    }
}
