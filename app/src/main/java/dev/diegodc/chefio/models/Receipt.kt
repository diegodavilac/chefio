package dev.diegodc.chefio.models

data class Receipt(
    val id: String,
    val title: String,
    val image: String,
    val isFavorite: Boolean = false,
    val timeToPrepare: Int = 0,
    val creator: UserProfile
)
