package dev.diegodc.chefio.features.receiptDetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.diegodc.chefio.DestinationsArgs
import dev.diegodc.chefio.common.base.BaseViewModel
import dev.diegodc.chefio.data.Result
import dev.diegodc.chefio.data.repositories.IRecipesRepository
import dev.diegodc.chefio.data.succeeded
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeDetailViewModel @Inject constructor(
    private val recipeRepository: IRecipesRepository,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<RecipeDetailViewState>() {

    private val recipeId: String? = savedStateHandle[DestinationsArgs.RECIPE_ID_ARG]
    override val initialState: RecipeDetailViewState
        get() = RecipeDetailViewState()

    init {
        loadData()
    }

    private fun loadData() {
        if (recipeId != null) {
            _uiState.update {
                it.copy(isLoading = true)
            }

            viewModelScope.launch {
                val result = recipeRepository.getRecipeDetail(recipeId)

                if (result.succeeded) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            recipe = (result as Result.Success).data
                        )
                    }
                } else {
                    _uiState.update {
                        //SHOW ERROR
                        it.copy(isLoading = false)
                    }
                }
            }
        }
    }

}