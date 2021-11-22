package com.github.app.ui.main.repodetail

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.github.app.R
import com.github.app.databinding.RepoDetailActivityBinding
import com.github.app.domain.entity.Owner
import com.github.app.domain.entity.SearchRepo
import com.github.app.extension.loadUrl
import com.github.app.reduce.Action
import com.github.app.ui.base.BaseViewActivity
import com.github.app.ui.main.userdetail.UserDetailActivity
import com.github.app.util.Keys
import dagger.hilt.android.AndroidEntryPoint
import splitties.activities.start
import splitties.views.onClick
import timber.log.Timber

@AndroidEntryPoint
class RepoDetailActivity : BaseViewActivity<RepoDetailViewModel, RepoDetailActivityBinding>() {

    private val generateVM: RepoDetailViewModel by viewModels()

    override fun provideViewModel() = generateVM

    override val layoutRes = R.layout.repo_detail_activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeViewModel()
    }

    private fun initializeViewModel() {
        viewModel.apply {
            with(binding) {
                val repo = intent.getParcelableExtra<SearchRepo>(Keys.AVATAR_REPO)
                repo?.owner?.ownerImageUrl?.let { repoDetailPosterIV.loadUrl(it) }
                repoDetailTitleTV.text = "Repo Name : " + repo?.repoName
                repoDetailEmailTV.text = "Owner Email : " + repo?.owner?.email
                repoDetaiForkCountTV.text = "Fork Count : " + repo?.forks.toString()

                observableState.observe(this@RepoDetailActivity, Observer {
                    with(it) {
                        when {
                            isLoadError -> Timber.d("error")
                            isLoading -> Timber.d("loading")
                            isLoaded -> gotoUserDetail(it.data)
                        }
                    }

                })
                repoDetailPosterIV.onClick {
                    repo?.owner?.ownerName?.let { Action.ActionUserDetail(it) }
                        ?.let { dispatch(it) }
                }
            }
        }
    }

    private fun gotoUserDetail(data: Any) {
        data as Owner
        start<UserDetailActivity> {
            putExtra(Keys.AVATAR_URL, data.ownerImageUrl)
            putExtra(Keys.AVATAR_NAME, data.ownerName)
            putExtra(Keys.AVATAR_EMAIL, data.email)
            finish()
        }
    }
}