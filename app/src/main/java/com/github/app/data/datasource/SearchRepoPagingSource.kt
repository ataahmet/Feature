package com.github.app.data.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.github.app.data.repository.mapper.mapToSearchListEntity
import com.github.app.data.source.remote.api.GithubApi
import com.github.app.domain.entity.SearchRepo
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class SearchRepoPagingSource(
    private val githubApi: GithubApi,
) : PagingSource<Int, SearchRepo>() {
    override fun getRefreshKey(state: PagingState<Int, SearchRepo>): Int? {
        return state.anchorPosition?.let { anchor ->
            state.closestPageToPosition(anchor)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchor)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SearchRepo> {
        val page = params.key ?: 1
        return try {
            val result =
                suspendCancellableCoroutine { cont ->
                    val disposable =
                        githubApi
                            .repositories(params.loadSize, page)
                            .subscribe(
                                { cont.resume(it) },
                                { cont.resumeWithException(it) },
                            )
                    cont.invokeOnCancellation { disposable.dispose() }
                }
            val items = result.mapToSearchListEntity()
            LoadResult.Page(
                data = items,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (items.isEmpty()) null else page + 1,
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
