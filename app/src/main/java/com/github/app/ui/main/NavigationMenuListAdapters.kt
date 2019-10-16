package com.github.app.ui.main

import android.view.ViewGroup
import com.github.app.R
import com.github.app.databinding.NavigationItemBinding
import com.github.app.domain.entity.NavigationItem
import com.github.app.extension.getInflater
import com.github.app.ui.base.BaseAdapter
import com.github.app.ui.base.BaseViewHolder
import splitties.views.textColorResource

class NavigationMenuListAdapters :
    BaseAdapter<NavigationItem, NavigationMenuListViewHolder>() {

    override fun onCreateViewHolder(root: ViewGroup) =
        NavigationMenuListViewHolder(
            NavigationItemBinding.inflate(
                root.getInflater(),
                root,
                false
            )
        )

    fun updateItem(data: NavigationItem) {
        if (!data.isSelected) {
            currentList.map { it.isSelected = false }
            currentList[currentList.indexOf(data)].isSelected = true
        }

        notifyDataSetChanged()
    }
}

class NavigationMenuListViewHolder(
    navigationMenuListBinding: NavigationItemBinding
) :
    BaseViewHolder<NavigationItem, NavigationItemBinding>(navigationMenuListBinding) {

    override fun bindData(data: NavigationItem) {
        binding.apply {
            if (data.isSelected) {
                navMenuItemTv.textColorResource = R.color.white
            } else {
                navMenuItemTv.textColorResource = android.R.color.darker_gray
            }
            navMenuItemTv.text = data.value
        }
    }
}





