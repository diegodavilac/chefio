package dev.diegodc.chefio.data.sources.recipe

import android.net.Uri
import com.google.firebase.firestore.DocumentSnapshot
import dev.diegodc.chefio.data.models.PagedRecipes
import dev.diegodc.chefio.models.Recipe

interface RecipeDataSource {
    suspend fun createOrUpdateRecipe(recipe: Recipe ) : String
    suspend fun loadRecipes(clean:Boolean) : List<Recipe>
    suspend fun loadRecipesFromFirebase(key: DocumentSnapshot?, query: String?) : PagedRecipes
    suspend fun loadRecipeDetail(recipeId: String): Recipe
    suspend fun uploadRecipePhoto(pathToFile: Uri) : String
}