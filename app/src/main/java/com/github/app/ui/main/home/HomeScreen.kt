package com.github.app.ui.main.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.github.app.domain.entity.Owner
import com.github.app.domain.entity.SearchRepo
import com.github.app.reduce.Action
import com.github.app.reduce.State
import com.github.app.ui.components.SearchRepoCard

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onRepoClick: (SearchRepo) -> Unit,
    onOwnerClick: (Owner) -> Unit
) {
    val state by viewModel.observableState.observeAsState(State(isIdle = true))
    val pagingItems = viewModel.searchRepoPagingFlow.collectAsLazyPagingItems()

    LaunchedEffect(state.isLoaded) {
        if (state.isLoaded) {
            (state.data as? Owner)?.let { onOwnerClick(it) }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(
                count = pagingItems.itemCount,
                key = pagingItems.itemKey { it.id }
            ) { index ->
                pagingItems[index]?.let { repo ->
                    SearchRepoCard(
                        repo = repo,
                        onImageClick = { viewModel.dispatch(Action.ActionUserDetail(it.owner?.ownerName ?: "")) },
                        onCardClick = { onRepoClick(it) }
                    )
                }
            }

            when (pagingItems.loadState.append) {
                is LoadState.Loading -> item {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is LoadState.Error -> item {
                    Text(
                        text = "Yükleme hatası",
                        modifier = Modifier.padding(16.dp)
                    )
                }
                else -> Unit
            }
        }

        if (pagingItems.loadState.refresh is LoadState.Loading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}
