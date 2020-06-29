package com.mieszko.employeesmanager.domain.usecase

import com.mieszko.employeesmanager.domain.model.EmployeeProfile
import com.mieszko.employeesmanager.domain.repository.EmployeeProfileRepository
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

interface GetEmployeeProfileUseCase {
    operator fun invoke(id: Int): Single<EmployeeProfile>
}

class GetEmployeeProfileUseCaseImpl(
    private val employeeProfileRepository: EmployeeProfileRepository,
    private val getAccessTokenUseCase: GetAccessTokenUseCase
) :
    GetEmployeeProfileUseCase {

    override operator fun invoke(id: Int): Single<EmployeeProfile> {
        return getAccessTokenUseCase()
            .flatMap { accessToken ->
                employeeProfileRepository
                    .getEmployeeProfile(id, accessToken)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
            }

    }
}
