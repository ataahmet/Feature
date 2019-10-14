package com.github.app.domain.usecase.getItemsNavigation

import com.github.app.domain.entity.NavigationItem
import com.github.app.domain.repository.NavigationRepository
import javax.inject.Inject

class ProdGetNavigationItemUseCase @Inject constructor(
    private val navigationRepository: NavigationRepository
) : GetNavigationItemUseCase {
    override fun execute(): List<NavigationItem> {
        val list = navigationRepository.getNavigationsItem().toMutableList()
        return list
    }
}