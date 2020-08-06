package com.mieszko.employeesmanager.domain.usecase

import com.mieszko.employeesmanager.TestUtil.anyNonNull
import com.mieszko.employeesmanager.application.di.repositoryDependency
import com.mieszko.employeesmanager.domain.model.AccessToken
import com.mieszko.employeesmanager.domain.repository.AuthRepository
import io.reactivex.Single
import junit.framework.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.inject
import org.koin.test.mock.MockProviderRule
import org.koin.test.mock.declareMock
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.verify
import org.mockito.Mockito
import org.mockito.Mockito.times
import java.util.*

class GetAccessTokenUseCaseImplTest : KoinTest {
    @get:Rule
    val koinTestRule = KoinTestRule.create {
        modules(repositoryDependency)
    }

    // required to make your Mock via Koin
    @get:Rule
    val mockProvider = MockProviderRule.create { clazz ->
        Mockito.mock(clazz.java)
    }

    private val authRepository: AuthRepository by inject()

    @Test
    fun `token is not cached on failed fetch`() {
        val repoMock = declareMock<AuthRepository> {
            given(this.getToken()).willReturn(Single.error(Throwable("getToken error")))
            given(this.cacheToken(anyNonNull())).will { Mockito.doNothing() }
        }

        verify(repoMock, times(0)).cacheToken(anyNonNull())
    }

    @Test
    fun `token is returned and cached on successful fetch`() {
        val accessToken = AccessToken("test_token", Calendar.getInstance().timeInMillis)

        val repoMock = declareMock<AuthRepository> {
            given(this.getToken()).willReturn(Single.just(accessToken))
            given(this.cacheToken(accessToken)).will { Mockito.doNothing() }
        }

        assertEquals(accessToken, GetAccessTokenUseCaseImpl(authRepository).invoke().blockingGet())
        verify(repoMock, times(1)).cacheToken(accessToken)
    }
}