package dev.diegodc.chefio.data.sources.recipe

import android.net.Uri
import android.util.Log
import dev.diegodc.chefio.data.repositories.IRecipesRepository
import dev.diegodc.chefio.models.Recipe
import dev.diegodc.chefio.data.Result
import dev.diegodc.chefio.models.UserProfile
import kotlinx.coroutines.*
import java.util.*
import kotlin.coroutines.suspendCoroutine

class RecipeRepository(
    val remoteDataSource: RecipeDataSource,
    val dispatcher: CoroutineDispatcher
) : IRecipesRepository {
    override suspend fun saveRecipe(recipe: Recipe, pathToFile: Uri?): Result<String> =
        withContext(dispatcher) {
            try {
                val url =
                    if (pathToFile != null)
                        remoteDataSource.uploadRecipePhoto(pathToFile)
                    else ""

                val result = remoteDataSource.createOrUpdateRecipe(recipe = recipe.copy(image = url))

                Result.Success(result)
            } catch (e: Exception) {
                Result.Error(e)
            }
        }

    override suspend fun getRecipe(page: Int): Result<List<Recipe>> {
        return try {
            Result.Success(remoteDataSource.loadRecipes(page <= 1))
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun deleteRecipe(receiptId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getRecipeDetail(recipeId: String): Result<Recipe> {
        return try {
            Result.Success(remoteDataSource.loadRecipeDetail(recipeId))
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}