package dev.diegodc.chefio.data.repositories

import android.net.Uri
import dev.diegodc.chefio.models.Recipe
import dev.diegodc.chefio.data.Result

interface IRecipesRepository {
    suspend fun saveRecipe(recipe: Recipe, pathToFile: Uri?): Result<String>
    suspend fun getRecipe(page:Int) : Result<List<Recipe>>
    suspend fun deleteRecipe(receiptId: String)
    suspend fun getRecipeDetail(recipeId: String): Result<Recipe>
}