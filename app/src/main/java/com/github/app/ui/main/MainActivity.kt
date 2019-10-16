package com.github.app.ui.main

import android.os.Bundle
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import com.github.app.R
import com.github.app.databinding.MainActivityBinding
import com.github.app.helper.NavController
import com.github.app.ui.base.BaseViewActivity
import splitties.views.onClick

class MainActivity : BaseViewActivity<MainViewModel, MainActivityBinding>() {
    override val layoutRes = R.layout.main_activity
    override val viewModelClass = MainViewModel::class.java

    private val navController: NavController by lazy { NavController(R.id.container, this) }

    private val navigationMenuListAdapters: NavigationMenuListAdapters by lazy {
        NavigationMenuListAdapters().apply {
            clickListener = {
                navController.navigateMain(it).apply {
                    closeDrawerLayout()
                    navigationMenuListAdapters.updateItem(it)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.apply {
            with(binding) {
                viewModel.getNavigationItemLiveData.observe(this@MainActivity, Observer {
                    navigationMenuItemList.adapter = navigationMenuListAdapters
                    navigationMenuListAdapters.submitList(it)
                    appBar.navDrawIv.onClick { openDrawerLayout() }

                })
            }

        }
    }

    private fun openDrawerLayout() {
        binding.drawerLayout.openDrawer(GravityCompat.START)
    }

    private fun closeDrawerLayout() {
        binding.drawerLayout.closeDrawers()
    }
}



