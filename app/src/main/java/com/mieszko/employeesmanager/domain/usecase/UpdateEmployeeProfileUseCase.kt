package com.mieszko.employeesmanager.domain.usecase

import com.mieszko.employeesmanager.domain.model.EmployeeProfile
import com.mieszko.employeesmanager.domain.repository.EmployeeProfileRepository
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

interface UpdateEmployeeProfileUseCase {
    operator fun invoke(updatedProfile: EmployeeProfile): Completable
}

class UpdateEmployeeProfileUseCaseImpl(
    private val employeeProfileRepository: EmployeeProfileRepository,
    private val getAccessTokenUseCase: GetAccessTokenUseCase
) : UpdateEmployeeProfileUseCase {

    override operator fun invoke(
        updatedProfile: EmployeeProfile
    ): Completable {
        return getAccessTokenUseCase()
            .flatMapCompletable { accessToken ->
                employeeProfileRepository
                    .updateEmployeeProfile(updatedProfile, accessToken)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
            }
    }
}
