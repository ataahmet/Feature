package com.github.app.data.repository

import com.github.app.data.repository.mapper.mapToEntity
import com.github.app.data.source.navigation.ProdNavigationDataSource
import com.github.app.domain.entity.NavigationItem
import com.github.app.domain.repository.NavigationRepository
import javax.inject.Inject

class ProdNavigationRepository @Inject constructor(private val prodNavigationDataSource: ProdNavigationDataSource) :
    NavigationRepository {
    override fun getNavigationsItem(): List<NavigationItem> {
        return mapToEntity(prodNavigationDataSource.getNavigationItems())
    }
}

