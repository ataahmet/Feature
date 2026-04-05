package com.github.app.ui.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.github.app.R
import com.github.app.domain.entity.Owner
import com.github.app.domain.entity.SearchRepo
import com.github.app.ui.base.ComposeBaseActivity
import com.github.app.ui.main.home.HomeScreen
import com.github.app.ui.main.home.HomeViewModel
import com.github.app.ui.main.repodetail.RepoDetailActivity
import com.github.app.ui.main.userdetail.UserDetailActivity
import com.github.app.util.Keys
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import splitties.activities.start
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.hilt.navigation.compose.hiltViewModel

@AndroidEntryPoint
class MainActivity : ComposeBaseActivity<MainViewModel>() {

    private val generateVM: MainViewModel by viewModels()

    override fun provideViewModel() = generateVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navItems by viewModel.getNavigationItemLiveData.observeAsState(emptyList())
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        val navController = rememberNavController()

        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet {
                    Box(
                        modifier = Modifier
                            .size(width = 240.dp, height = 120.dp)
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.mipmap.github_logo),
                            contentDescription = "Github Logo",
                            modifier = Modifier.size(100.dp),
                            tint = Color.Unspecified
                        )
                    }
                    HorizontalDivider()
                    LazyColumn {
                        items(navItems) { item ->
                            NavigationDrawerItem(
                                label = { Text(item.value) },
                                selected = item.isSelected,
                                onClick = {
                                    scope.launch { drawerState.close() }
                                },
                                colors = NavigationDrawerItemDefaults.colors(
                                    selectedTextColor = Color.White,
                                    unselectedTextColor = Color.Gray
                                )
                            )
                        }
                    }
                }
            }
        ) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { },
                        navigationIcon = {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(
                                    imageVector = Icons.Filled.Menu,
                                    contentDescription = "Menü"
                                )
                            }
                        }
                    )
                }
            ) { paddingValues ->
                NavHost(
                    navController = navController,
                    startDestination = "home",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    composable("home") {
                        val homeViewModel: HomeViewModel = hiltViewModel()
                        HomeScreen(
                            viewModel = homeViewModel,
                            onRepoClick = { repo -> gotoRepoDetail(repo) },
                            onOwnerClick = { owner -> gotoUserDetail(owner) }
                        )
                    }
                }
            }
        }
    }

    private fun gotoRepoDetail(repo: SearchRepo) {
        start<RepoDetailActivity> {
            putExtra(Keys.AVATAR_REPO, repo)
        }
    }

    private fun gotoUserDetail(owner: Owner) {
        start<UserDetailActivity> {
            putExtra(Keys.AVATAR_URL, owner.ownerImageUrl)
            putExtra(Keys.AVATAR_NAME, owner.ownerName)
            putExtra(Keys.AVATAR_EMAIL, owner.email)
        }
    }
}
