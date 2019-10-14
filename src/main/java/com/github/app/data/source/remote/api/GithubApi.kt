package com.github.app.data.source.remote.api

import com.github.app.data.source.remote.api.entity.OwnerItem
import com.github.app.data.source.remote.api.entity.SearchRepoItem
import com.github.app.data.source.remote.api.entity.UserReposItem
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubApi {

    @GET("search/repositories?q=retrofit")
    fun repositories(@Query("per_page") id: Int, @Query("page") pageIndex: Int): Single<SearchRepoItem>

    @GET("users/{owner_name}")
    fun getUserDetail(@Path("owner_name") ownerName: String): Single<OwnerItem>

    @GET("users/{owner_name}/repos")
    fun getUserRepositoriesDetail(
        @Path("owner_name") ownerName: String,
        @Query("per_page") id: Int,
        @Query("page") pageIndex: Int
    )
        : Single<List<UserReposItem>>

}