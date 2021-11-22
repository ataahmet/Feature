package com.github.app.ui.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import com.github.app.R
import com.github.app.databinding.MainActivityBinding
import com.github.app.helper.NavController
import com.github.app.ui.base.BaseViewActivity
import dagger.hilt.android.AndroidEntryPoint
import splitties.views.onClick

@AndroidEntryPoint
class MainActivity : BaseViewActivity<MainViewModel, MainActivityBinding>() {

    private val generateVM : MainViewModel by viewModels()
    private val navController: NavController by lazy { NavController(R.id.container, this) }

    override val layoutRes = R.layout.main_activity
    override fun provideViewModel() = generateVM

    private val navigationMenuListAdapters: NavigationMenuListAdapters by lazy {
        NavigationMenuListAdapters().apply {
            clickListener = {
                navController.navigateMain(it).apply {
                    closeDrawerLayout()
                    openDrawerLayout()
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

    private fun closeDrawerLayout() {
        binding.drawerLayout.closeDrawers()
    }

    private fun openDrawerLayout() {
        binding.drawerLayout.openDrawer(GravityCompat.START)
    }


}



