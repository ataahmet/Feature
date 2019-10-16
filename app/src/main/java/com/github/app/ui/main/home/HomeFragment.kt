@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.github.app.ui.main.home

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.app.R
import com.github.app.databinding.HomeFragmentBinding
import com.github.app.domain.entity.Owner
import com.github.app.domain.entity.SearchRepo
import com.github.app.reduce.Action
import com.github.app.ui.base.BaseFragment
import com.github.app.ui.main.repodetail.RepoDetailActivity
import com.github.app.ui.main.userdetail.UserDetailActivity
import com.github.app.util.Keys.AVATAR_EMAIL
import com.github.app.util.Keys.AVATAR_NAME
import com.github.app.util.Keys.AVATAR_REPO
import com.github.app.util.Keys.AVATAR_URL
import splitties.activities.start
import timber.log.Timber

class HomeFragment : BaseFragment<HomeViewModel, HomeFragmentBinding>() {

    override val layoutRes = R.layout.home_fragment
    override val viewModelClass = HomeViewModel::class.java

    private val searchRepoListAdapter: SearchRepoListAdapter by lazy {
        SearchRepoListAdapter().apply {
            clickImageListener = {
                viewModel.apply {
                    dispatch(Action.ActionUserDetail(it.owner?.ownerName!!))
                }
            }
            clickListener = {
                gotoRepoDetail(it)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViewModel()
    }

    private fun initializeViewModel() {
        viewModel.apply {
            with(binding) {
                repoList.apply {
                    setHasFixedSize(true)
                    layoutManager = LinearLayoutManager(activity)
                    adapter = searchRepoListAdapter
                }
                liveDataSearchRepoList.observe(viewLifecycleOwner, Observer {
                    searchRepoListAdapter.submitList(it)

                })
                observableState.observe(viewLifecycleOwner, Observer {
                    with(it) {
                        when {
                            isLoadError -> Timber.d("error")
                            isLoading -> Timber.d("loading")
                            isLoaded -> gotoUserDetail(it.data)
                        }
                    }

                })
            }
        }
    }

    private fun gotoRepoDetail(data: SearchRepo) {
        activity?.start<RepoDetailActivity> {
            putExtra(AVATAR_REPO, data)
        }
    }

    private fun gotoUserDetail(data: Any) {
        data as Owner
        activity?.start<UserDetailActivity> {
            putExtra(AVATAR_URL, data.ownerImageUrl)
            putExtra(AVATAR_NAME, data.ownerName)
            putExtra(AVATAR_EMAIL, data.email)
        }
    }
}