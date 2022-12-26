package dev.diegodc.chefio.features.addEditReceipt

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.diegodc.chefio.DestinationsArgs
import dev.diegodc.chefio.features.addEditReceipt.models.AddEditReceiptState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class AddEditReceiptViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel(){
    private val receiptId: String? = savedStateHandle[DestinationsArgs.RECEIPT_ID_ARG]

    // Init state
    private val _uiState = MutableStateFlow(AddEditReceiptState())
    val uiState: StateFlow<AddEditReceiptState> = _uiState.asStateFlow()


    fun updateTitle(newTitle: String) {
        _uiState.update {
            it.copy(title = newTitle)
        }
    }

    fun updateDescription(newDescription: String) {
        _uiState.update {
            it.copy(description = newDescription)
        }
    }

    fun updateTimeToPrepare(newTime: Float) {
        _uiState.update {
            it.copy(timeToPrepared = newTime)
        }
    }

    fun isAvailableNextStep() : Boolean{
        return _uiState.value.title.isNotEmpty() && _uiState.value.description.isNotEmpty() && _uiState.value.timeToPrepared > 0
    }
}