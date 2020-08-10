package com.mieszko.employeesmanager.domain.usecase

import com.mieszko.employeesmanager.domain.model.AccessToken
import com.mieszko.employeesmanager.domain.repository.AuthRepository
import com.mieszko.employeesmanager.domain.util.SchedulerProvider
import io.reactivex.Single

interface GetAccessTokenUseCase {
    operator fun invoke(): Single<AccessToken>
}

class GetAccessTokenUseCaseImpl(
    private val authRepository: AuthRepository,
    private val schedulerProvider: SchedulerProvider
) :
    GetAccessTokenUseCase {

    override operator fun invoke(): Single<AccessToken> {
        return authRepository.getToken()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.io())
            .doOnSuccess { newToken ->
                authRepository.cacheToken(newToken)
            }
    }
}