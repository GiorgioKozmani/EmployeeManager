package com.mieszko.employeesmanager.data.repository

import com.mieszko.employeesmanager.data.mappers.mapEmployeeProfile
import com.mieszko.employeesmanager.data.model.response.EmployeeProfileResponse
import com.mieszko.employeesmanager.data.source.remote.ApiHeaders
import com.mieszko.employeesmanager.data.source.remote.ApiHeadersProvider
import com.mieszko.employeesmanager.data.source.remote.api.EmployeeProfileApi
import com.mieszko.employeesmanager.domain.model.AccessToken
import com.mieszko.employeesmanager.domain.model.EmployeeProfile
import com.mieszko.employeesmanager.domain.model.Gender
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class EmployeeProfileRepositoryImplTest {

    private var testProfileApi = Mockito.mock(EmployeeProfileApi::class.java)
    private var testHeadersProvider = Mockito.mock(ApiHeadersProvider::class.java)
    private var testHeaders = Mockito.mock(ApiHeaders.Authenticated::class.java)
    private var testAccessToken = Mockito.mock(AccessToken::class.java)
    private val testProfile =
        EmployeeProfile(1337, "test_first_name", "test_last_name", Gender.Female)
    private val testProfileDTO = mapEmployeeProfile(testProfile)

    @Before
    fun setUp() {
        testProfileApi = Mockito.mock(EmployeeProfileApi::class.java)
        testHeadersProvider = Mockito.mock(ApiHeadersProvider::class.java)
        testHeaders = Mockito.mock(ApiHeaders.Authenticated::class.java)
        testAccessToken = Mockito.mock(AccessToken::class.java)

        Mockito
            .`when`(testHeadersProvider.getAuthenticatedHeaders(testAccessToken))
            .thenReturn(testHeaders)
    }

    @Test
    fun `getEmployeeProfile returns profile domain model on network success`() {
        mockGetProfileApiResponse(Single.just(EmployeeProfileResponse(testProfileDTO)))

        createGetProfileTestObserver().assertValue(testProfile)
    }

    @Test
    fun `getEmployeeProfile returns unmapped throwable on network error`() {
        val throwable = Throwable("network error")

        mockGetProfileApiResponse(Single.error(throwable))

        createGetProfileTestObserver().assertError(throwable)
    }

    @Test
    fun `updateEmployeeProfile should send network request with DTO`() {
        Mockito
            .`when`(
                testProfileApi.updateEmployeeProfile(testHeaders, testProfileDTO.id, testProfileDTO)
            )
            .thenReturn(Completable.complete())

        EmployeeProfileRepositoryImpl(testProfileApi, testHeadersProvider)
            .updateEmployeeProfile(testProfile, testAccessToken)
            .test()

        Mockito.verify(testProfileApi, Mockito.times(1))
            .updateEmployeeProfile(testHeaders, testProfileDTO.id, testProfileDTO)
    }

    private fun createGetProfileTestObserver(): TestObserver<EmployeeProfile> {
        return EmployeeProfileRepositoryImpl(testProfileApi, testHeadersProvider)
            .getEmployeeProfile(testProfileDTO.id, testAccessToken)
            .test()
    }

    private fun mockGetProfileApiResponse(response: Single<EmployeeProfileResponse>) {
        Mockito
            .`when`(testProfileApi.getEmployeeProfile(testHeaders, testProfileDTO.id))
            .thenReturn(response)
    }
}