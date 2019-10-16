package com.github.app.domain.usecase.getItemsNavigation

import com.github.app.domain.entity.NavigationItem
import com.github.app.domain.usecase.UseCase

interface GetNavigationItemUseCase : UseCase {
    fun execute(): List<NavigationItem>
}