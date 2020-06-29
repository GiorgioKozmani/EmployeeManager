package com.mieszko.employeesmanager.domain.usecase

import com.mieszko.employeesmanager.domain.model.AccessToken
import com.mieszko.employeesmanager.domain.repository.AuthRepository
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

interface GetAccessTokenUseCase {
    operator fun invoke(): Single<AccessToken>
}

class GetAccessTokenUseCaseImpl(private val authRepository: AuthRepository) :
    GetAccessTokenUseCase {

    override operator fun invoke(): Single<AccessToken> {
        return authRepository.getToken()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .doOnSuccess { newToken ->
                authRepository.cacheToken(newToken)
            }
    }
}