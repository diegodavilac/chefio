package dev.diegodc.chefio.features.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.diegodc.chefio.data.repositories.IRecipesRepository
import dev.diegodc.chefio.features.home.models.HomeViewState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import dev.diegodc.chefio.data.Result
import dev.diegodc.chefio.data.sources.recipe.RecipeDataSource
import dev.diegodc.chefio.data.sources.recipe.remote.RecipePagingSource
import dev.diegodc.chefio.data.sources.recipe.remote.RecipeRemoteDataSource.Companion.PAGE_SIZE
import dev.diegodc.chefio.data.succeeded
import dev.diegodc.chefio.di.RemoteDataSource
import dev.diegodc.chefio.models.Recipe
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val recipeRepository: IRecipesRepository,
    @RemoteDataSource private val remoteDataSource: RecipeDataSource,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    // Init state
    private val _uiState = MutableStateFlow(HomeViewState())
    val uiState: StateFlow<HomeViewState> = _uiState.asStateFlow()

    private lateinit var pagingSource: RecipePagingSource

    private var searchJob : Job? = null

    init {
        loadData()
    }

    val recipesPager = Pager(
        pagingSourceFactory = { RecipePagingSource(remoteDataSource, query = _uiState.value.query).also {
            pagingSource = it
        } },
        config = PagingConfig(
            pageSize = PAGE_SIZE.toInt()
        )
    ).flow.cachedIn(viewModelScope)

    fun updateLoading(isLoading: Boolean) {
        _uiState.update {
            it.copy(isLoading = isLoading)
        }
    }

    fun updateQuery(text: String) {
        searchJob?.cancel()
        _uiState.update {
            it.copy(
                query = text
            )
        }
        viewModelScope.launch {
            delay(1500L)
            pagingSource.invalidate()
        }
    }

    fun updateRecipesLoaded(recipes: List<Recipe>) {
        _uiState.update {
            it.copy(
                recipes = recipes
            )
        }
    }

    fun updatePage(page: Int) {
        _uiState.update {
            it.copy(
                page = page
            )
        }
    }

    fun loadData() {
        updateLoading(true)
        updatePage( 1)
        viewModelScope.launch {
            val result = recipeRepository.getRecipe(_uiState.value.page)
            handleLoadData(result)
        }
    }

    fun loadMoreData(){
        updateLoading(true)
        updatePage( _uiState.value.page + 1)
    }

    private fun handleLoadData(result: Result<List<Recipe>>) {
        updateLoading(false)
        if (result.succeeded) {
            updateRecipesLoaded((result as Result.Success).data)
        } else {
            updateRecipesLoaded(emptyList())
        }
    }
}