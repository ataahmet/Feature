package com.github.app.ui.main.repodetail

import android.os.Bundle
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.app.domain.entity.Owner
import com.github.app.domain.entity.SearchRepo
import com.github.app.reduce.Action
import com.github.app.reduce.State
import com.github.app.ui.base.ComposeBaseActivity
import com.github.app.ui.components.RepoImage
import com.github.app.ui.main.userdetail.UserDetailActivity
import com.github.app.util.Keys
import dagger.hilt.android.AndroidEntryPoint
import splitties.activities.start

@AndroidEntryPoint
class RepoDetailActivity : ComposeBaseActivity<RepoDetailViewModel>() {
    private val generateVM: RepoDetailViewModel by viewModels()

    override fun provideViewModel() = generateVM

    private val repo: SearchRepo? by lazy {
        @Suppress("DEPRECATION")
        intent.getParcelableExtra(Keys.AVATAR_REPO)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @Composable
    override fun Content() {
        val state by viewModel.observableState.observeAsState(State(isIdle = true))

        LaunchedEffect(state.isLoaded) {
            if (state.isLoaded) {
                (state.data as? Owner)?.let { gotoUserDetail(it) }
            }
        }

        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(16.dp),
        ) {
            RepoImage(
                url = repo?.owner?.ownerImageUrl,
                modifier =
                    Modifier
                        .size(120.dp)
                        .clickable {
                            repo?.owner?.ownerName?.let {
                                viewModel.dispatch(Action.ActionUserDetail(it))
                            }
                        },
            )
            Text(
                text = "Repo Name : ${repo?.repoName ?: ""}",
                fontSize = 18.sp,
                modifier = Modifier.padding(top = 16.dp),
            )
            Text(
                text = "Owner Email : ${repo?.owner?.email ?: ""}",
                fontSize = 16.sp,
                modifier = Modifier.padding(top = 8.dp),
            )
            Text(
                text = "Fork Count : ${repo?.forks?.toString() ?: "0"}",
                fontSize = 16.sp,
                modifier = Modifier.padding(top = 8.dp),
            )
        }
    }

    private fun gotoUserDetail(data: Owner) {
        start<UserDetailActivity> {
            putExtra(Keys.AVATAR_URL, data.ownerImageUrl)
            putExtra(Keys.AVATAR_NAME, data.ownerName)
            putExtra(Keys.AVATAR_EMAIL, data.email)
        }
        finish()
    }
}
