package dev.diegodc.chefio.features.addEditRecipe.models

import android.location.Location
import android.net.Uri

data class AddEditRecipeState(
    val title: String = "",
    val description: String = "",
    val timeToPrepared: Float = 0f,
    val step: Int = 1,
    val totalStep: Int = 2,
    val totalIngredients: Int = 2,
    val totalPreparationSteps: Int = 1,
    val isLoading: Boolean = false,
    val isSaved: Boolean = false,
    val isCompleted: Boolean = false,
    val ingredients: List<String> = listOf("", ""),
    val preparationSteps: List<String> = listOf(""),
    val photoPath : Uri? = null,
    val location: Location? = null,
    val address: String = "",
) {

    val isAvailableNextStep : Boolean
    get() {
        return when (step) {
            1 -> title.isNotEmpty() && description.isNotEmpty() && timeToPrepared > 0
            2 -> true
            else -> false
        }
    }
}
