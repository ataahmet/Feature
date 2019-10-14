package com.github.app.ui.main.home

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.github.app.R
import com.github.app.data.datasource.NetworkState
import com.github.app.databinding.NetworkRetryItemBinding
import com.github.app.databinding.SearchRepoItemBinding
import com.github.app.domain.entity.SearchRepo
import com.github.app.extension.getInflater
import com.github.app.extension.loadUrl
import com.github.app.ui.base.BaseViewHolder
import splitties.views.onClick

class SearchRepoListAdapter :
    PagedListAdapter<SearchRepo, RecyclerView.ViewHolder>(REPO_COMPARATOR) {

    private var networkState: NetworkState? = null

    var clickImageListener: (searchRepo: SearchRepo) -> Unit = {}

    var clickListener: (searchRepo: SearchRepo) -> Unit = {}

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.search_repo_item -> (holder as SearchRepoViewHolder).bindData(getItem(position)!!)
            else -> (holder as NetworkStateViewHolder).bindData("")
        }
    }

    private fun hasExtraRow(): Boolean = networkState != null && networkState != NetworkState.LOADED

    override fun getItemViewType(position: Int): Int {
        if (hasExtraRow() && position == itemCount - 1) {
            return R.layout.network_retry_item
        } else {
            return R.layout.search_repo_item
        }
    }

    override fun onCreateViewHolder(root: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.search_repo_item -> SearchRepoViewHolder(
                SearchRepoItemBinding.inflate(
                    root.getInflater(),
                    root,
                    false
                ), clickImageListener, clickListener
            )
            else -> NetworkStateViewHolder(
                NetworkRetryItemBinding.inflate(
                    root.getInflater(),
                    root,
                    false
                )
            )
        }
    }

    fun setNetworkState(newNetworkState: NetworkState?) {
        if (currentList != null) {
            if (currentList!!.size != 0) {
                val previousState = this.networkState
                val hadExtraRow = hasExtraRow()
                this.networkState = newNetworkState
                val hasExtraRow = hasExtraRow()
                if (hadExtraRow != hasExtraRow) {
                    if (hadExtraRow) {
                        notifyItemRemoved(super.getItemCount())
                    } else {
                        notifyItemInserted(super.getItemCount())
                    }
                } else if (hasExtraRow && previousState !== newNetworkState) {
                    notifyItemChanged(itemCount - 1)
                }
            }
        }
    }

    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<SearchRepo>() {
            override fun areItemsTheSame(oldItem: SearchRepo, newItem: SearchRepo): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: SearchRepo, newItem: SearchRepo): Boolean =
                oldItem == newItem
        }
    }

    class SearchRepoViewHolder(
        searchRepoItemBinding: SearchRepoItemBinding,
        val clickImageListener: (searchRepo: SearchRepo) -> Unit,
        val clickListener: (searchRepo: SearchRepo) -> Unit
    ) :
        BaseViewHolder<SearchRepo, SearchRepoItemBinding>(searchRepoItemBinding) {

        override fun bindData(data: SearchRepo) {
            binding.apply {
                data.let {
                    ownerIV.loadUrl(data.owner?.ownerImageUrl!!)
                    repoTittleTv.text = data.repoName
                    ownerIV.onClick {
                        clickImageListener(data)
                    }
                    repoCV.onClick {
                        clickListener(data)
                    }

                }

            }
        }
    }
}

class NetworkStateViewHolder(networkRetryItemBinding: NetworkRetryItemBinding) :
    BaseViewHolder<String, NetworkRetryItemBinding>(networkRetryItemBinding) {

    override fun bindData(data: String) {
        binding.apply {

        }
    }
}


