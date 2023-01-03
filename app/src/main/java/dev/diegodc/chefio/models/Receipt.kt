package dev.diegodc.chefio.models

import java.util.Date

data class Receipt(
    val id: String,
    val title: String,
    val image: String,
    val isFavorite: Boolean = false,
    val timeToPrepare: Int = 0,
    val creator: UserProfile,
    val ingredients: List<String> = listOf(),
    val preparationSteps : List<PreparationStep> = listOf(),
    val createdAt: Date
)
