package com.mieszko.employeesmanager.application.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.mieszko.employeesmanager.domain.model.Employee
import com.mieszko.employeesmanager.domain.usecase.GetEmployeesPagingSourceUseCase
import com.mieszko.employeesmanager.domain.usecase.GetEmployeesPagingSourceUseCaseImpl
import org.koin.java.KoinJavaComponent.get

class ListViewModel(
    private var employeesPagingSource: GetEmployeesPagingSourceUseCase,
    private var pagingConfig: PagingConfig
) : ViewModel() {

    var employeePagerLiveData = createPager()

    fun employeeUpdated(id: Int) {
        recreatePagingSource()
    }

    private fun recreatePagingSource() {
        employeesPagingSource = get(GetEmployeesPagingSourceUseCase::class.java)
        employeePagerLiveData = createPager()
        employeesPagingSource.invalidate()
    }

    private fun createPager(): LiveData<PagingData<Employee>> {
        return Pager(
            config = pagingConfig,
            initialKey = 0,
            remoteMediator = null,
            pagingSourceFactory = { employeesPagingSource as GetEmployeesPagingSourceUseCaseImpl })
            .liveData
            .cachedIn(viewModelScope)
    }

}