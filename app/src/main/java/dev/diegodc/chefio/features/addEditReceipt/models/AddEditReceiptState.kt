package dev.diegodc.chefio.features.addEditReceipt.models

data class AddEditReceiptState(
    val title: String = "",
    val description: String = "",
    val timeToPrepared: Float = 0f,
    val step: Int = 1,
    val totalStep: Int = 2,
    val isLoading: Boolean = false,
    val isSaved: Boolean = false,
    val isCompleted: Boolean = false,
)
