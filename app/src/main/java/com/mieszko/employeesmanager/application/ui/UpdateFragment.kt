package com.mieszko.employeesmanager.application.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.jakewharton.rxbinding2.widget.RxTextView
import com.mieszko.employeesmanager.R
import com.mieszko.employeesmanager.application.viewmodel.ProfileFieldChange
import com.mieszko.employeesmanager.application.viewmodel.SharedViewModel
import com.mieszko.employeesmanager.application.viewmodel.UpdateViewModel
import com.mieszko.employeesmanager.domain.model.EmployeeProfile
import com.mieszko.employeesmanager.domain.model.Gender
import com.mieszko.employeesmanager.domain.model.Resource
import kotlinx.android.synthetic.main.update_fragment.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class UpdateFragment : Fragment() {
    private val sharedViewModel: SharedViewModel by sharedViewModel()
    private val viewModel: UpdateViewModel by viewModel {
        parametersOf(
            arguments?.getInt(
                CLICKED_LIST_ITEM_ID
            ) ?: -1
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.update_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        setUpdateClickListener()
        setupFieldsChangeListeners()
        initDynamicListSpinner()
    }

    private fun observeViewModel() {
        observeEmployeeToEdit()
        observeUpdateState()
    }

    private fun setupFieldsChangeListeners() {
        RxTextView.textChanges(first_name_edit_text)
            .skipInitialValue()
            .map { ProfileFieldChange.FirstNameChange(it.toString()) }
            .subscribe(viewModel.inputChange)

        RxTextView.textChanges(last_name_edit_text)
            .skipInitialValue()
            .map { ProfileFieldChange.LastNameChange(it.toString()) }
            .subscribe(viewModel.inputChange)

        gender_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                viewModel.inputChange.onNext(ProfileFieldChange.GenderChange(null))
            }

            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                viewModel.inputChange.onNext(ProfileFieldChange.GenderChange(Gender.values()[position]))
            }
        }
    }

    private fun setUpdateClickListener() {
        employee_details_button
            .setOnClickListener {
                viewModel.onUpdateButtonClick()
            }
    }

    private fun observeEmployeeToEdit() {
        viewModel
            .employeeProfileLiveData
            .observe(
                viewLifecycleOwner,
                Observer {
                    handleEmployeeToEditResponse(it)
                }
            )
    }

    private fun observeUpdateState() {
        viewModel
            .updateStateLiveData
            .observe(
                viewLifecycleOwner,
                Observer {
                    handleUpdateState(it)
                }
            )
    }

    private fun initDynamicListSpinner() {
        val adapter: ArrayAdapter<String> = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            Gender.values().map { it.name }
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        gender_spinner.adapter = adapter
    }

    private fun setupInitialValues(employeeProfileResponse: Resource.Success<EmployeeProfile>) {
        first_name_edit_text.setText(employeeProfileResponse.data.firstName)
        last_name_edit_text.setText(employeeProfileResponse.data.lastName)
        employeeProfileResponse.data.gender?.ordinal?.let { gender_spinner.setSelection(it) }
    }

    private fun handleEmployeeToEditResponse(employeeProfileResponse: Resource<EmployeeProfile>) {
        when (employeeProfileResponse) {
            is Resource.Success -> {
                employee_details_button.isEnabled = true
                employee_details_loading_overlay.isVisible = false

                setupInitialValues(employeeProfileResponse)
            }
            is Resource.Loading -> {
                employee_details_button.isEnabled = false
                employee_details_loading_overlay.isVisible = true
            }
            is Resource.Error -> {
                employee_details_button.isEnabled = true
                employee_details_loading_overlay.isVisible = false
            }
        }
    }

    private fun handleUpdateState(updateResponse: Resource<Int>) {
        when (updateResponse) {
            is Resource.Success -> {
                with(employee_details_button) {
                    isEnabled = true
                    text = getString(R.string.update_employee)
                }
                Toast.makeText(context, getString(R.string.update_completed), Toast.LENGTH_SHORT).show()
                // Inform list fragment that it has to update its data
                sharedViewModel.employeeUpdated(updateResponse.data)
            }
            is Resource.Loading -> {
                with(employee_details_button) {
                    isEnabled = false
                    text = getString(R.string.updating)
                }
            }
            is Resource.Error -> {
                with(employee_details_button) {
                    isEnabled = true
                    text = getString(R.string.update_employee)
                }
                Toast.makeText(context, getString(R.string.update_error), Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        const val CLICKED_LIST_ITEM_ID = "employee_from_list"
    }
}