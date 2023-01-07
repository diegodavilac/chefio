package dev.diegodc.chefio.data.models

import com.google.firebase.firestore.DocumentSnapshot
import dev.diegodc.chefio.models.Recipe

data class PagedRecipes(
    val key: DocumentSnapshot? = null,
    val data: List<Recipe> = emptyList(),
    val error: String? = null
)
