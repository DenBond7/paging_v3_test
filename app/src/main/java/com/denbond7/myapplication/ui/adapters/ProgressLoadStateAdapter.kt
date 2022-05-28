package com.denbond7.myapplication.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.denbond7.myapplication.R
import com.denbond7.myapplication.databinding.LoadUsersProgressBinding

/**
 * @author Denis Bondarenko
 *         Date: 5/28/22
 *         Time: 12:05 PM
 *         E-mail: DenBond7@gmail.com
 */
class ProgressLoadStateAdapter : LoadStateAdapter<ProgressLoadStateAdapter.ViewHolder>() {
    override fun onBindViewHolder(holder: ViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.load_users_progress, parent, false)
        )
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding: LoadUsersProgressBinding = LoadUsersProgressBinding.bind(itemView)

        fun bind(loadState: LoadState) {
            binding.progressBar.visibility =
                if (loadState is LoadState.Loading) View.VISIBLE else View.GONE
        }
    }
}
