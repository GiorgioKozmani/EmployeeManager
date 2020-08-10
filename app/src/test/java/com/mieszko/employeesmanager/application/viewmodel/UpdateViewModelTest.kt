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
    fun `on view model creation profile loading event is emitted before success event`() {
        // Don't emit value before 1000ms from subscription
        val testScheduler = TestScheduler(1000, TimeUnit.MILLISECONDS)

        Mockito
            .`when`(testGetProfileUseCase.invoke(testProfile.id))
            .thenReturn(Single.just(testProfile).observeOn(testScheduler))

        viewModel = createViewModel()

        assertEquals(
            Resource.Loading(null),
            viewModel.employeeProfileLiveData.getOrAwaitValue(0)
        )

        testScheduler.advanceTimeBy(2000, TimeUnit.MILLISECONDS)

        assertEquals(
            Resource.Success(testProfile),
            viewModel.employeeProfileLiveData.getOrAwaitValue(0)
        )
    }

    @Test
    fun `profile changes are being uploaded on update button clicked`() {
        //todo split test into smaller chunks with factored out parts

        Mockito
            .`when`(testGetProfileUseCase.invoke(testProfile.id))
            .thenReturn(Single.just(testProfile))

        Mockito
            .`when`(testUpdateProfileUseCase.invoke(anyNonNull()))
            .thenReturn(Completable.complete())

        viewModel = createViewModel()

        val firstNameChange = ProfileFieldChange.FirstNameChange("changed_f_name")
        val lastNameChange = ProfileFieldChange.LastNameChange("changed_l_name")
        val genderChange = ProfileFieldChange.GenderChange(Gender.Male)

        listOf(
            firstNameChange,
            lastNameChange,
            genderChange
        )
            .forEach {
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

    private fun createViewModel(): UpdateViewModel {
        return UpdateViewModel(
            testProfile.id,
            testUpdateProfileUseCase,
            testGetProfileUseCase
        )
    }
}