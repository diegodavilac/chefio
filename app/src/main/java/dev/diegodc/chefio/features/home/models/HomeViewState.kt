package dev.diegodc.chefio.features.home.models

import dev.diegodc.chefio.models.Receipt

data class HomeViewState(
    val receipts : List<Receipt> = emptyList(),
    val page: Int = 1,
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
)
