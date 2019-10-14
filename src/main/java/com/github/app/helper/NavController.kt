package com.github.app.helper

import android.app.Activity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.github.app.R
import com.github.app.domain.entity.NavigationItem

class NavController(private val viewId: Int, private val activity: Activity) {
    companion object {
        const val githup = 0
        var CURRENT_DESTINATION = R.id.home_fragment
    }


    private val navController: NavController by lazy { activity.findNavController(viewId) }

    fun navigateMain(navItem: NavigationItem) {
        navController.apply {
            popBackStack()
            when (navItem.id) {
                githup -> this@NavController.navigate(R.id.home_fragment)
            }
        }
    }

    private fun navigate(destId: Int) {
        navController.apply {
            if (CURRENT_DESTINATION != destId) {
                navigate(destId)
                CURRENT_DESTINATION = destId;
            }
        }
    }
}





