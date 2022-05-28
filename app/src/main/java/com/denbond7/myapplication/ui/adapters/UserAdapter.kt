package com.denbond7.myapplication.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.denbond7.myapplication.R
import com.denbond7.myapplication.database.User
import com.denbond7.myapplication.databinding.ItemUserBinding

/**
 * @author Denis Bondarenko
 *         Date: 5/27/22
 *         Time: 6:30 PM
 *         E-mail: DenBond7@gmail.com
 */
class UserAdapter : PagingDataAdapter<User, UserAdapter.UserViewHolder>(UserComparator) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        )
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding: ItemUserBinding = ItemUserBinding.bind(itemView)
        fun bind(user: User?) {
            binding.textViewId.text =
                itemView.context.getString(R.string.uid_template, user?.uid ?: 0)
            binding.textViewFirstName.text = user?.firstName
            binding.textViewLastName.text = user?.lastName
        }
    }

    companion object UserComparator : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.uid == newItem.uid
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }
    }
}
