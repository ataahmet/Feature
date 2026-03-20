package com.github.app.ui.base

import android.content.Context
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

abstract class BasePagedListAdapter<D : Any, VH : BaseViewHolder2<D, ViewDataBinding>> :
    PagingDataAdapter<D, VH>(DiffCallback<D>()) {

    var clickListener: (D) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        onCreateViewHolder(parent)

    override fun onBindViewHolder(holder: VH, position: Int) {
        val data = getItem(position) ?: return  // ✅ Paging 3'te getItem kullanılır
        holder.apply {
            bindData(data)
            itemView.setOnClickListener { clickListener(data) }
        }
    }

    abstract fun onCreateViewHolder(root: ViewGroup): VH
}

abstract class BaseViewHolder2<D, out B : ViewDataBinding>(
    protected val binding: B
) : RecyclerView.ViewHolder(binding.root) {
    protected val context: Context = binding.root.context
    abstract fun bindData(data: D)
}

class DiffCallback2<D : Any> : DiffUtil.ItemCallback<D>() {
    override fun areItemsTheSame(oldItem: D, newItem: D): Boolean = oldItem == newItem
    override fun areContentsTheSame(oldItem: D, newItem: D): Boolean = oldItem == newItem
}