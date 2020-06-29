package com.mieszko.employeesmanager.application.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mieszko.employeesmanager.R
import com.mieszko.employeesmanager.application.ui.UpdateFragment.Companion.CLICKED_LIST_ITEM_ID
import com.mieszko.employeesmanager.application.ui.adapter.EmployeesAdapter
import com.mieszko.employeesmanager.application.ui.adapter.EmployeesLoadStateAdapter
import com.mieszko.employeesmanager.application.viewmodel.ListViewModel
import com.mieszko.employeesmanager.application.viewmodel.SharedViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.net.UnknownHostException

class ListFragment : Fragment() {
    private val disposablesBag = CompositeDisposable()

    private val sharedViewModel: SharedViewModel by sharedViewModel()
    private val viewModel: ListViewModel by viewModel()

    private val listAdapter = EmployeesAdapter()
    private val recyclerView by lazy { requireView().findViewById<RecyclerView>(R.id.employees_list) }
    private val progressBar by lazy { requireView().findViewById<ProgressBar>(R.id.progress_bar) }
    private val retryButton by lazy { requireView().findViewById<Button>(R.id.retry_button) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeViewModel()
        setRetryButtonListener()
    }

    override fun onResume() {
        super.onResume()
        disposablesBag.add(
            listAdapter
                .itemClicked
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    findNavController()
                        .navigate(
                            R.id.to_update_employeem_action,
                            bundleOf(CLICKED_LIST_ITEM_ID to it.id)
                        )
                })
    }

    override fun onPause() {
        super.onPause()
        disposablesBag.clear()
        listAdapter.removeLoadStateListener(stateChangeListener)
    }

    private fun observeViewModel() {
        observePagerData()
        observeEmployeeUpdates()
    }

    private fun observePagerData() {
        viewModel
            .employeePagerLiveData
            .observe(
                viewLifecycleOwner,
                Observer { pagingData ->
                    listAdapter.submitData(lifecycle, pagingData)
                }
            )
    }

    private fun observeEmployeeUpdates() {
        sharedViewModel
            .employeeUpdatedLiveData
            // update event occurs when this fragment is not displayed, therefore there's a need to use observeForever
            .observeForever { id ->
                viewModel.employeeUpdated(id)
            }
    }

    private fun setupRecyclerView() {
        view
            ?.findViewById<RecyclerView>(R.id.employees_list)
            ?.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = listAdapter.withLoadStateFooter(
                    footer = EmployeesLoadStateAdapter {
                        listAdapter.retry()
                    }
                )
            }

        listAdapter.addLoadStateListener(listener = stateChangeListener)
    }

    private fun setRetryButtonListener() {
        retryButton?.setOnClickListener {
            listAdapter.retry()
        }
    }

    private val stateChangeListener = { loadStates: CombinedLoadStates ->
        recyclerView.isVisible = loadStates.refresh is LoadState.NotLoading
        progressBar.isVisible = loadStates.refresh is LoadState.Loading
        retryButton.isVisible = loadStates.refresh is LoadState.Error

        (loadStates.refresh as? LoadState.Error)?.let {
            Toast.makeText(
                context,
                getString(
                    when (it.error) {
                        is UnknownHostException -> R.string.internet_connection_error
                        else -> R.string.could_not_load_content_error
                    }
                ),
                Toast.LENGTH_LONG
            ).show()
        }
        Unit
    }
}
