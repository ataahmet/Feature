package com.github.app.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.app.domain.entity.SearchRepo

@Composable
fun SearchRepoCard(
    repo: SearchRepo,
    onImageClick: (SearchRepo) -> Unit = {},
    onCardClick: (SearchRepo) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(8.dp)
            .clickable { onCardClick(repo) },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            RepoImage(
                url = repo.owner?.ownerImageUrl,
                modifier = Modifier
                    .size(48.dp)
                    .clickable { onImageClick(repo) }
            )
            Column(modifier = Modifier.padding(start = 8.dp)) {
                Text(text = repo.repoName ?: "", fontSize = 14.sp)
            }
        }
    }
}
