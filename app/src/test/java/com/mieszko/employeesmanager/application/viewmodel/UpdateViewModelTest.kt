package com.mieszko.employeesmanager.application.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.mieszko.employeesmanager.TestUtil.anyNonNull
import com.mieszko.employeesmanager.TestUtil.getOrAwaitValue
import com.mieszko.employeesmanager.domain.model.EmployeeProfile
import com.mieszko.employeesmanager.domain.model.Gender
import com.mieszko.employeesmanager.domain.model.Resource
import com.mieszko.employeesmanager.domain.usecase.GetEmployeeProfileUseCase
import com.mieszko.employeesmanager.domain.usecase.UpdateEmployeeProfileUseCase
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.TestScheduler
import junit.framework.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import java.util.concurrent.TimeUnit

class UpdateViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    private var testGetProfileUseCase = Mockito.mock(GetEmployeeProfileUseCase::class.java)
    private var testUpdateProfileUseCase = Mockito.mock(UpdateEmployeeProfileUseCase::class.java)
    private var testProfile = EmployeeProfile(10, "test_f_name", "test_l_name", Gender.Female)
    private lateinit var viewModel: UpdateViewModel

    @Test
    fun `emits profile loading and success on successful profile fetch`() {
        val testScheduler = TestScheduler()
        mockGetProfileUseCase(
            Single
                .just(testProfile)
                .delay(1000, TimeUnit.MILLISECONDS, testScheduler)
        )
        viewModel = createViewModel()

        testScheduler.advanceTimeTo(999, TimeUnit.MILLISECONDS)

        assertEquals(
            Resource.Loading(null),
            viewModel.employeeProfileLiveData.getOrAwaitValue(0)
        )

        testScheduler.advanceTimeTo(1001, TimeUnit.MILLISECONDS)

        assertEquals(
            Resource.Success(testProfile),
            viewModel.employeeProfileLiveData.getOrAwaitValue(0)
        )
    }

    @Test
    fun `emits profile loading and error on failed profile fetch`() {
        val testScheduler = TestScheduler()
        val testThrowable = Throwable()
        mockGetProfileUseCase(
            Single
                .error<EmployeeProfile>(testThrowable)
                .delay(1000, TimeUnit.MILLISECONDS, testScheduler, true)
        )
        viewModel = createViewModel()

        testScheduler.advanceTimeTo(999, TimeUnit.MILLISECONDS)

        assertEquals(
            Resource.Loading(null),
            viewModel.employeeProfileLiveData.getOrAwaitValue(0)
        )

        testScheduler.advanceTimeTo(1001, TimeUnit.MILLISECONDS)

        assertEquals(
            Resource.Error<EmployeeProfile>(testThrowable),
            viewModel.employeeProfileLiveData.getOrAwaitValue(0)
        )
    }

    @Test
    fun `profile changes are being uploaded on update button clicked`() {
        mockGetProfileUseCase(Single.just(testProfile))
        mockProfileUpdateUseCase(Completable.complete())
        viewModel = createViewModel()

        val firstNameChange = ProfileFieldChange.FirstNameChange("changed_f_name")
        val lastNameChange = ProfileFieldChange.LastNameChange("changed_l_name")
        val genderChange = ProfileFieldChange.GenderChange(Gender.Male)

        listOf(
            firstNameChange,
            lastNameChange,
            genderChange
        ).forEach {
            viewModel.inputChange.onNext(it)
        }

        viewModel.onUpdateButtonClick()

        Mockito
            .verify(testUpdateProfileUseCase, Mockito.times(1))
            .invoke(
                EmployeeProfile(
                    testProfile.id,
                    firstNameChange.firstName,
                    lastNameChange.lastName,
                    genderChange.gender
                )
            )
    }

    @Test
    fun `emits update loading and success on successful update`() {
        val testScheduler = TestScheduler()
        mockGetProfileUseCase(Single.just(testProfile))
        mockProfileUpdateUseCase(
            Completable
                .complete()
                .delay(1000, TimeUnit.MILLISECONDS, testScheduler)
        )
        viewModel = createViewModel()
        viewModel.onUpdateButtonClick()

        testScheduler.advanceTimeTo(999, TimeUnit.MILLISECONDS)

        assertEquals(
            Resource.Loading(null),
            viewModel.updateStateLiveData.getOrAwaitValue(0)
        )

        testScheduler.advanceTimeTo(1001, TimeUnit.MILLISECONDS)

        assertEquals(
            Resource.Success(testProfile.id),
            viewModel.updateStateLiveData.getOrAwaitValue(0)
        )
    }

    @Test
    fun `emits update loading and error on failed update`() {
        val testScheduler = TestScheduler()
        val testThrowable = Throwable()
        mockGetProfileUseCase(Single.just(testProfile))
        mockProfileUpdateUseCase(
            Completable
                .error(testThrowable)
                .delay(1000, TimeUnit.MILLISECONDS, testScheduler, true)
        )
        viewModel = createViewModel()

        viewModel.onUpdateButtonClick()

        testScheduler.advanceTimeTo(999, TimeUnit.MILLISECONDS)

        assertEquals(
            Resource.Loading(null),
            viewModel.updateStateLiveData.getOrAwaitValue(0)
        )

        testScheduler.advanceTimeTo(1001, TimeUnit.MILLISECONDS)

        assertEquals(
            Resource.Error<Int>(testThrowable),
            viewModel.updateStateLiveData.getOrAwaitValue(0)
        )
    }

    private fun mockProfileUpdateUseCase(updateCompletable: Completable) {
        Mockito
            .`when`(testUpdateProfileUseCase.invoke(anyNonNull()))
            .thenReturn(updateCompletable)
    }

    private fun mockGetProfileUseCase(profileSingle: Single<EmployeeProfile>) {
        Mockito
            .`when`(testGetProfileUseCase.invoke(testProfile.id))
            .thenReturn(profileSingle)
    }

    private fun createViewModel(): UpdateViewModel {
        return UpdateViewModel(
            testProfile.id,
            testUpdateProfileUseCase,
            testGetProfileUseCase
        )
    }
}