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
    private var pagingConfig: PagingConfig
) : ViewModel() {

    val employeePagerLiveData = createPager()
    private lateinit var currentPagingSource: GetEmployeesPagingSourceUseCaseImpl

    fun employeeUpdated(id: Int) {
        currentPagingSource.invalidate()
    }

    private fun createPager(): LiveData<PagingData<Employee>> {
        return Pager(
            config = pagingConfig,
            initialKey = 0,
            remoteMediator = null,
            pagingSourceFactory = {
                (get(GetEmployeesPagingSourceUseCase::class.java) as GetEmployeesPagingSourceUseCaseImpl)
                    .also {
                        currentPagingSource = it
                    }
            })
            .liveData
            .cachedIn(viewModelScope)
    }

}