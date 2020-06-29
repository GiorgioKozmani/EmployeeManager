package com.mieszko.employeesmanager.application.ui.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.mieszko.employeesmanager.R
import java.net.UnknownHostException


class LoadStateViewHolder private constructor(
    itemView: View,
    retry: () -> Unit
) : RecyclerView.ViewHolder(itemView) {

    private var mProgressBar: ProgressBar = itemView.findViewById(R.id.progress_bar)
    private var mRetry: Button = itemView.findViewById(R.id.retry_button)

    init {
        mRetry.setOnClickListener { retry.invoke() }
    }

    fun bind(loadState: LoadState) {
        if (loadState is LoadState.Error) {
            showErrorToast(loadState)
        }
        mProgressBar.isVisible = loadState is LoadState.Loading
        mRetry.isVisible = loadState !is LoadState.Loading
    }

    private fun showErrorToast(errorState: LoadState.Error) {
        val errorMsgRes = when (errorState.error) {
            is UnknownHostException -> R.string.internet_connection_error
            else -> R.string.could_not_load_content_error
        }

        Toast.makeText(
            itemView.context,
            itemView.context.getString(errorMsgRes),
            Toast.LENGTH_SHORT
        ).show()
    }

    companion object {
        fun create(parent: ViewGroup, retry: () -> Unit): LoadStateViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.load_state_item, parent, false)
            return LoadStateViewHolder(view, retry)
        }
    }
}