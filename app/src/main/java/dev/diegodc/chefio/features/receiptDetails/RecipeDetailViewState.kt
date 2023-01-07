package dev.diegodc.chefio.features.receiptDetails

import dev.diegodc.chefio.common.base.BaseViewState
import dev.diegodc.chefio.models.Recipe

data class RecipeDetailViewState(
    val isLoading : Boolean = false,
    val recipe: Recipe? = null
) : BaseViewState
