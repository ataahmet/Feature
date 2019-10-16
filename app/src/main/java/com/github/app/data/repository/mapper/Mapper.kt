package com.github.app.data.repository.mapper

import com.github.app.data.source.remote.api.entity.OwnerItem
import com.github.app.data.source.remote.api.entity.SearchRepoItem
import com.github.app.data.source.remote.api.entity.UserReposItem
import com.github.app.domain.entity.Owner
import com.github.app.domain.entity.SearchRepo

fun SearchRepoItem.mapToSearchListEntity() = `items`?.map {
    it?.mapToSearchRepoEntity()
} ?: mutableListOf()

fun SearchRepoItem.RepoItem.mapToSearchRepoEntity() = SearchRepo(
    id = id,
    repoName = name,
    forks = forks,
    owner = owner?.mapToOwnerEntity()

)

fun OwnerItem.mapToOwnerEntity() = Owner(
    ownerImageUrl = avatar_url,
    ownerName = login,
    email = blog /*email tum kullanıcılarda null geliyor du blog olarak değiştirdim*/
)

fun UserReposItem.mapToUserRepoEntity() = SearchRepo(
    id = id,
    repoName = name,
    forks = forks,
    owner = owner?.mapToOwnerEntity()

)

fun maptoUserRepoListEntity(item: List<UserReposItem>): List<SearchRepo> {
    return item.map {
        it.mapToUserRepoEntity()
    }
}



