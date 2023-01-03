package dev.diegodc.chefio.features.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.diegodc.chefio.data.repositories.IReceiptsRepository
import dev.diegodc.chefio.features.home.models.HomeViewState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import dev.diegodc.chefio.data.Result
import dev.diegodc.chefio.data.succeeded
import dev.diegodc.chefio.models.Receipt

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val receiptRepository: IReceiptsRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    // Init state
    private val _uiState = MutableStateFlow(HomeViewState())
    val uiState: StateFlow<HomeViewState> = _uiState.asStateFlow()

    init {
        loadData()
    }


    fun updateLoading(isLoading: Boolean) {
        _uiState.update {
            it.copy(isLoading = isLoading)
        }
    }

    fun updateReceiptsLoaded(receipts: List<Receipt>) {
        _uiState.update {
            it.copy(
                receipts = receipts
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
            val result = receiptRepository.getReceipts(_uiState.value.page)
            handleLoadData(result)
        }
    }

    fun loadMoreData(){
        updateLoading(true)
        updatePage( _uiState.value.page + 1)
    }

    private fun handleLoadData(result: Result<List<Receipt>>) {
        updateLoading(false)
        if (result.succeeded) {
            updateReceiptsLoaded((result as Result.Success).data)
        } else {
            updateReceiptsLoaded(emptyList())
        }
    }
}