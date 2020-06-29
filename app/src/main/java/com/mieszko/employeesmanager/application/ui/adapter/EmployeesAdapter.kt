package com.mieszko.employeesmanager.application.ui.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.mieszko.employeesmanager.application.ui.viewholder.EmployeeViewHolder
import com.mieszko.employeesmanager.domain.model.Employee
import io.reactivex.subjects.PublishSubject
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.Main

class EmployeesAdapter :
    PagingDataAdapter<Employee, EmployeeViewHolder>(
        UserComparator(), Main, Default
    ) {

    val itemClicked = PublishSubject.create<Employee>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeViewHolder {
        return EmployeeViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: EmployeeViewHolder, position: Int) {
        val employee = getItem(position)
        holder.bind(employee) {
            if (employee != null) {
                itemClicked.onNext(employee)
            }
        }
    }

    internal class UserComparator : DiffUtil.ItemCallback<Employee>() {
        override fun areItemsTheSame(
            oldItem: Employee, newItem: Employee
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Employee, newItem: Employee
        ): Boolean {
            return oldItem == newItem
        }
    }
}