package com.example.githubuser.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.githubuser.data.remote.User
import com.example.githubuser.databinding.ItemUserBinding

class UserAdapter: RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private var list = ArrayList<User>()

    private var onItemClickCallback: OnItemClickCallback? = null
    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun updateList(newList: ArrayList<User>) {
        val userDiff = UserDiffutils(list, newList)
        val result = DiffUtil.calculateDiff(userDiff)
        list = newList
        result.dispatchUpdatesTo(this)
    }

    inner class UserViewHolder(private val binding: ItemUserBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            binding.apply {
                root.setOnClickListener {
                    onItemClickCallback?.onItemClicked(user)
                }
                tvName.text = user.login
                tvHtmlUrl.text = user.htmlUrl
                Glide.with(itemView)
                    .load(user.avatarUrl)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .centerCrop()
                    .into(ivUser)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    interface OnItemClickCallback{
        fun onItemClicked(data: User)
    }
}