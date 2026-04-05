package com.github.app.ui.main.userdetail

import android.os.Bundle
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.github.app.ui.base.ComposeBaseActivity
import com.github.app.ui.components.RepoImage
import com.github.app.ui.components.SearchRepoCard
import com.github.app.util.Keys.AVATAR_EMAIL
import com.github.app.util.Keys.AVATAR_NAME
import com.github.app.util.Keys.AVATAR_URL
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserDetailActivity : ComposeBaseActivity<UserDetailViewModel>() {

    private val generateVM: UserDetailViewModel by viewModels()

    override fun provideViewModel() = generateVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val avatarUrl = intent.getStringExtra(AVATAR_URL)
        val ownerName = intent.getStringExtra(AVATAR_NAME)
        val ownerEmail = intent.getStringExtra(AVATAR_EMAIL)
        val pagingItems = viewModel.searchRepoPagingFlow.collectAsLazyPagingItems()

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(ownerName ?: "") },
                    navigationIcon = {
                        IconButton(onClick = { finish() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Geri"
                            )
                        }
                    }
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                RepoImage(
                    url = avatarUrl,
                    modifier = Modifier.size(120.dp)
                )
                Text(
                    text = ownerName ?: "",
                    fontSize = 20.sp,
                    modifier = Modifier.padding(top = 12.dp)
                )
                Text(
                    text = ownerEmail ?: "",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
                LazyRow(modifier = Modifier.padding(top = 16.dp)) {
                    items(
                        count = pagingItems.itemCount,
                        key = pagingItems.itemKey { it.id }
                    ) { index ->
                        pagingItems[index]?.let { repo ->
                            SearchRepoCard(repo = repo)
                        }
                    }
                }
            }
        }
    }
}
