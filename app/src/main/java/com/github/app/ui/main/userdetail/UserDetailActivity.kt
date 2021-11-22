package com.github.app.ui.main.userdetail

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.app.R
import com.github.app.databinding.UserDetailActivityBinding
import com.github.app.extension.loadUrl
import com.github.app.ui.base.BaseViewActivity
import com.github.app.ui.main.home.SearchRepoListAdapter
import com.github.app.ui.main.repodetail.RepoDetailViewModel
import com.github.app.util.Keys.AVATAR_EMAIL
import com.github.app.util.Keys.AVATAR_NAME
import com.github.app.util.Keys.AVATAR_URL
import dagger.hilt.android.AndroidEntryPoint
import splitties.views.onClick

@AndroidEntryPoint
class UserDetailActivity : BaseViewActivity<UserDetailViewModel, UserDetailActivityBinding>() {

    override val layoutRes = R.layout.user_detail_activity

    private val generateVM: UserDetailViewModel by viewModels()

    override fun provideViewModel() = generateVM

    private val userDetailRepoListAdapter: SearchRepoListAdapter by lazy {
        SearchRepoListAdapter().apply {
            clickImageListener = {}
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.apply {
            val avatarImage: String? = intent.getStringExtra(AVATAR_URL)
            val ownerName: String? = intent.getStringExtra(AVATAR_NAME)
            val ownerEmail: String? = intent.getStringExtra(AVATAR_EMAIL)

            avatarImage?.let {
                userDetailPosterIV.loadUrl(avatarImage)
            }

            ownerEmail?.let {
                userDetailEmailTV.text = ownerEmail
            }

            ownerName?.let {
                userDetailTitleTV.text = ownerName
            }

            repoList.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(
                    this@UserDetailActivity,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
                adapter = userDetailRepoListAdapter
            }

            repoDetailTbar.setNavigationIcon(R.drawable.ic_left_arrow)
            repoDetailTbar.onClick { finish() }

        }
        initViewModel()
    }

    private fun initViewModel() {
        viewModel.apply {
            liveDataUserRepoList.observe(this@UserDetailActivity, Observer {
                userDetailRepoListAdapter.submitList(it)
            })
        }
    }
}