package com.mieszko.employeesmanager.domain.usecase

import com.mieszko.employeesmanager.TestUtil.anyNonNull
import com.mieszko.employeesmanager.domain.model.AccessToken
import com.mieszko.employeesmanager.domain.repository.AuthRepository
import io.reactivex.Single
import org.junit.Rule
import org.junit.Test
import org.koin.core.inject
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.mock.MockProviderRule
import org.koin.test.mock.declareMock
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.verify
import org.mockito.Mockito
import org.mockito.Mockito.times
import java.util.*

class GetAccessTokenUseCaseImplTest : KoinTest {

    //It avoids to use a AutoCloseKoinTest
    @get:Rule
    val koinTestRule = KoinTestRule.create {}

    // required to make your Mock via Koin
    @get:Rule
    val mockProvider = MockProviderRule.create { clazz ->
        Mockito.mock(clazz.java)
    }

    private val authRepository: AuthRepository by inject()

    @Test
    fun `token is returned and cached on successful fetch`() {
        val accessToken = AccessToken("test_token", Calendar.getInstance().timeInMillis)

        declareMock<AuthRepository> {
            given(this.getToken()).willReturn(Single.just(accessToken))
        }

        GetAccessTokenUseCaseImpl(authRepository)
            .invoke()
            .test()
            .await()
            .assertResult(accessToken)

        verify(authRepository, times(1)).cacheToken(accessToken)
    }

    @Test
    fun `exception is thrown and token is not cached on failed fetch`() {
        val thrownException = Throwable("test error")

        declareMock<AuthRepository> {
            given(this.getToken())
                .willReturn(Single.error(thrownException))
        }

        GetAccessTokenUseCaseImpl(authRepository)
            .invoke()
            .test()
            .await()
            .assertError(thrownException)

        verify(authRepository, times(0)).cacheToken(anyNonNull())
    }
}