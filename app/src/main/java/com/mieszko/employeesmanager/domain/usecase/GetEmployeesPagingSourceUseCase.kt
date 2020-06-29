package com.mieszko.employeesmanager.domain.usecase

import androidx.paging.PagingSource
import androidx.paging.rxjava2.RxPagingSource
import com.mieszko.employeesmanager.domain.model.Employee
import com.mieszko.employeesmanager.domain.repository.EmployeeRepository
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

interface GetEmployeesPagingSourceUseCase {
    operator fun invoke(params: PagingSource.LoadParams<Int>): Single<PagingSource.LoadResult<Int, Employee>>
    fun invalidate()
}

class GetEmployeesPagingSourceUseCaseImpl(
    private val employeeRepository: EmployeeRepository,
    private val getAccessTokenUseCase: GetAccessTokenUseCase,
    private val getDepartmentUseCase: GetDepartmentUseCase
) : GetEmployeesPagingSourceUseCase, RxPagingSource<Int, Employee>() {

    override operator fun invoke(params: LoadParams<Int>): Single<LoadResult<Int, Employee>> {
        return loadSingle(params)
    }

    @Suppress("USELESS_CAST")
    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, Employee>> {
        // params.key is an integer signifying last offset
        return getAccessTokenUseCase()
            .flatMap { accessToken ->
                employeeRepository
                    .getEmployees(
                        limit = params.loadSize,
                        offset = params.key,
                        accessToken = accessToken,
                        getDepartmentUseCase = getDepartmentUseCase
                    )
                    .map {
                        LoadResult.Page(
                            data = it,
                            nextKey = (params.key ?: 0) + params.loadSize,
                            prevKey = null // only caching forward
                        ) as LoadResult<Int, Employee>
                    }
                    .onErrorReturn { LoadResult.Error(it) }
                    .subscribeOn(Schedulers.io())
            }

    }

}