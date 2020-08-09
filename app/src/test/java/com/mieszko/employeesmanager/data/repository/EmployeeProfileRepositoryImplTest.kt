package com.mieszko.employeesmanager.data.repository

import com.mieszko.employeesmanager.data.mappers.mapEmployeeProfileDto
import com.mieszko.employeesmanager.data.model.dto.EmployeeProfileDTO
import com.mieszko.employeesmanager.data.model.response.EmployeeProfileResponse
import com.mieszko.employeesmanager.data.source.remote.ApiHeaders
import com.mieszko.employeesmanager.data.source.remote.ApiHeadersProvider
import com.mieszko.employeesmanager.data.source.remote.api.EmployeeProfileApi
import com.mieszko.employeesmanager.domain.model.AccessToken
import com.mieszko.employeesmanager.domain.model.EmployeeProfile
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
    private val testProfileDTO = EmployeeProfileDTO(
        1337,
        "test_first_name",
        "test_last_name",
        "Female"
    )

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
    fun `getEmployeeProfile should return profile domain model on network success`() {
        mockGetProfileApiResponse(Single.just(EmployeeProfileResponse(testProfileDTO)))

        createGetProfileTestObserver().assertValue(mapEmployeeProfileDto(testProfileDTO))
    }

    @Test
    fun `getEmployeeProfile should return unmapped throwable on network error`() {
        val throwable = Throwable("network error")

        mockGetProfileApiResponse(Single.error(throwable))

        createGetProfileTestObserver().assertError(throwable)
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