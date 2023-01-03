package dev.diegodc.chefio.features.addEditReceipt

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.diegodc.chefio.DestinationsArgs
import dev.diegodc.chefio.data.repositories.IReceiptsRepository
import dev.diegodc.chefio.features.addEditReceipt.models.AddEditReceiptState
import dev.diegodc.chefio.models.PreparationStep
import dev.diegodc.chefio.models.Receipt
import dev.diegodc.chefio.models.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class AddEditReceiptViewModel @Inject constructor(
    private val receiptRepository: IReceiptsRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val receiptId: String? = savedStateHandle[DestinationsArgs.RECEIPT_ID_ARG]

    // Init state
    private val _uiState = MutableStateFlow(AddEditReceiptState())
    val uiState: StateFlow<AddEditReceiptState> = _uiState.asStateFlow()

    init {
        if (receiptId != null) {
            //TODO : get receipt details to prefill
        }
    }

    fun updateIngredient(index: Int, ingredient: String) {
        if (index < uiState.value.ingredients.size) {
            _uiState.update {
                it.copy(
                    ingredients = it.ingredients.toMutableList().apply {
                        this[index] = ingredient
                    }
                )
            }
        }

    }

    fun updateStepPreparation(index: Int, step: String) {
        if (index < uiState.value.preparationSteps.size) {
            _uiState.update {
                it.copy(
                    preparationSteps = it.preparationSteps.toMutableList().apply {
                        this[index] = step
                    }
                )
            }
        }

    }


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

    fun goToNextPage() {
        _uiState.update {
            it.copy(
                step = it.step + 1
            )
        }
    }

    fun saveReceipt() {
        if (receiptId != null) {
            //TODO: edit receipt
        } else {
            viewModelScope.launch {
                val receipt = Receipt(
                    id = "",
                    title = _uiState.value.title,
                    createdAt = Date(),
                    image = "",
                    creator = UserProfile(
                        name = "",
                        lastName = "",
                        username = "",
                        photo = ""
                    ),
                    ingredients = _uiState.value.ingredients.filter { it.isNotEmpty() },
                    preparationSteps = _uiState.value.preparationSteps.filter { it.isNotEmpty() }.map {
                        PreparationStep(description = it)
                    }
                )
                receiptRepository.saveReceipt(receipt)
                _uiState.update {
                    it.copy(
                        isSaved = true
                    )
                }
            }
        }
    }

    fun addNewIngredient() {
        _uiState.update {
            it.copy(
                ingredients = it.ingredients.toMutableList().apply {
                    add("")
                }
            )
        }
    }

    fun addNewPreparationStep() {
        _uiState.update {
            it.copy(
                preparationSteps = it.preparationSteps.toMutableList().apply {
                    add("")
                }
            )
        }
    }
}