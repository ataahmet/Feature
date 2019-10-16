package com.github.app.domain.repository

import com.github.app.domain.entity.NavigationItem

interface NavigationRepository : Repository {
    fun getNavigationsItem(): List<NavigationItem>
}