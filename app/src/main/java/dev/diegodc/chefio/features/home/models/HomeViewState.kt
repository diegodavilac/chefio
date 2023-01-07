package dev.diegodc.chefio.features.home.models

import androidx.paging.Pager
import com.google.firebase.firestore.DocumentSnapshot
import dev.diegodc.chefio.models.Recipe

data class HomeViewState(
    val recipes : List<Recipe> = emptyList(),
    val page: Int = 1,
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val query: String = ""
)
