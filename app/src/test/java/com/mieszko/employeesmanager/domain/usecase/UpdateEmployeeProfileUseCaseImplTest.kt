package com.mieszko.employeesmanager.domain.usecase

import com.mieszko.employeesmanager.domain.model.AccessToken
import com.mieszko.employeesmanager.domain.model.EmployeeProfile
import com.mieszko.employeesmanager.domain.repository.EmployeeProfileRepository
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.core.inject
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.mock.MockProviderRule
import org.koin.test.mock.declareMock
import org.mockito.BDDMockito.given
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.Mockito.verify


class UpdateEmployeeProfileUseCaseImplTest : KoinTest {

    //It avoids to use a AutoCloseKoinTest
    @get:Rule
    val koinTestRule = KoinTestRule.create {}

    // required to make your Mock via Koin
    @get:Rule
    val mockProvider = MockProviderRule.create { clazz ->
        Mockito.mock(clazz.java)
    }

    private val profileRepository: EmployeeProfileRepository by inject()
    private val tokenUseCase: GetAccessTokenUseCase by inject()

    @Before
    fun setUp() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
    }

    @Test
    fun `usecase invoked updates profile`() {
        val tokenMock = Mockito.mock(AccessToken::class.java)
        val profileMock = Mockito.mock(EmployeeProfile::class.java)

        declareMock<GetAccessTokenUseCase> {
            given(this.invoke()).willReturn(Single.just(tokenMock))
        }
        declareMock<EmployeeProfileRepository> {
            given(this.updateEmployeeProfile(profileMock, tokenMock)).willReturn(
                Completable.complete()
            )
        }

        UpdateEmployeeProfileUseCaseImpl(profileRepository, tokenUseCase)
            .invoke(profileMock).test()

        verify(profileRepository, times(1))
            .updateEmployeeProfile(profileMock, tokenMock)
    }
}