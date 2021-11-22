package com.github.app.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.app.domain.entity.NavigationItem
import com.github.app.domain.usecase.getItemsNavigation.ProdGetNavigationItemUseCase
import com.github.app.reduce.Action
import com.github.app.reduce.State
import com.github.app.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    prodGetNavigationItemUseCase: ProdGetNavigationItemUseCase
) : BaseViewModel<Action, State>() {

    override fun bind() {}

    val getNavigationItemLiveData: LiveData<List<NavigationItem>> get() = _navigaitonItemsData

    private val _navigaitonItemsData = MutableLiveData<List<NavigationItem>>()

    init {
        _navigaitonItemsData.value = prodGetNavigationItemUseCase.execute()
    }

}