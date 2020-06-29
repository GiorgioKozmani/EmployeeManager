package com.mieszko.employeesmanager.application.ui.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mieszko.employeesmanager.R
import com.mieszko.employeesmanager.domain.model.Department
import com.mieszko.employeesmanager.domain.model.Employee

class EmployeeViewHolder private constructor(itemView: View) :
    RecyclerView.ViewHolder(itemView) {

    fun bind(currencyItem: Employee?, itemClickListener: () -> Unit) {
        currencyItem?.let {
            setFirstName(it.firstName)
            setLastName(it.lastName)
            setDepartments(it.departments)
        }

        // in case employee is null set listener to null
        itemView.setOnClickListener {
            currencyItem?.let { itemClickListener.invoke() }
        }
    }

    private fun setFirstName(firstName: String) {
        itemView.findViewById<TextView>(R.id.firstName).text = firstName
    }

    private fun setLastName(lastName: String) {
        itemView.findViewById<TextView>(R.id.lastName).text = lastName
    }

    private fun setDepartments(departments: List<Department>) {
        val departmentsLabel = itemView.findViewById<TextView>(R.id.departmentsLabel)
        val departmentsText = itemView.findViewById<TextView>(R.id.departments)
        if (departments.isEmpty()) {
            departmentsLabel.visibility = View.GONE
            departmentsText.visibility = View.GONE
        } else {
            departmentsLabel.visibility = View.VISIBLE
            departmentsText.visibility = View.VISIBLE
            itemView.findViewById<TextView>(R.id.departments).text =
                departments.joinToString(separator = ", ") { it.name }
        }
    }

    companion object {
        fun from(
            parent: ViewGroup
        ): EmployeeViewHolder {
            return EmployeeViewHolder(
                LayoutInflater
                    .from(parent.context)
                    .inflate(R.layout.employee_list_item, parent, false)
            )
        }
    }
}