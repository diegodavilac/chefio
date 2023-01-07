package dev.diegodc.chefio.data.sources.recipe.remote

import android.content.Context
import android.net.Uri
import android.util.Log
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.cloudinary.android.preprocess.BitmapEncoder
import com.cloudinary.android.preprocess.DimensionsValidator
import com.cloudinary.android.preprocess.ImagePreprocessChain
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import dev.diegodc.chefio.data.models.PagedRecipes
import dev.diegodc.chefio.data.sources.recipe.RecipeDataSource
import dev.diegodc.chefio.models.Recipe
import dev.diegodc.chefio.models.UserProfile
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class RecipeRemoteDataSource(
    private val firestore: FirebaseFirestore,
    val context: Context
) : RecipeDataSource {

    var lastReceiptVisible: DocumentSnapshot? = null

    companion object {
        const val PAGE_SIZE = 20L
        private const val RECIPES = "recipes"
    }

    override suspend fun createOrUpdateRecipe(recipe: Recipe): String {
        return if (recipe.id.isNotEmpty()) {
            firestore
                .collection(RECIPES)
                .document(recipe.id)
                .update(recipe.toFirebaseMap())
                .await()

            recipe.id
        } else {
            val result = firestore.collection(RECIPES)
                .add(recipe.toFirebaseMap())
                .await()

            result.id
        }
    }

    override suspend fun loadRecipes(clean: Boolean): List<Recipe> {
        if (clean) {
            lastReceiptVisible = null
        }

        val request = firestore.collection(RECIPES)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .limit(PAGE_SIZE)

        if (lastReceiptVisible != null) {
            request.startAfter(lastReceiptVisible)
        }

        val result = request.get().await()

        lastReceiptVisible = result.documents.last()
        return result.documents.map { snap ->
            val data = snap.data
            Recipe(
                id = snap.id,
                creator = UserProfile(
                    name = "",
                    lastName = "",
                    username = "",
                    photo = ""
                ),
                image = "",
                title = (data?.get("title") as String?) ?: "",
                createdAt = (data?.get("createdAt") as Timestamp).toDate(),
                description = (data["description"] as String?) ?: "",
                address = ""
            )
        }
    }

    override suspend fun loadRecipesFromFirebase(
        key: DocumentSnapshot?,
        query: String?
    ): PagedRecipes {
        try {
            val request = if (query.isNullOrEmpty())
                firestore.collection(RECIPES)
                    .orderBy("createdAt", Query.Direction.DESCENDING)
                    .limit(PAGE_SIZE + 1)
            else
                firestore.collection(RECIPES)
                    .whereEqualTo("title", query)
                    .orderBy("createdAt", Query.Direction.DESCENDING)
                    .limit(PAGE_SIZE + 1)


            if (key != null) {
                request.startAfter(key)
            }

            val result = request.get().await()

            var lastItem: DocumentSnapshot? = null

            if (result.documents.size > PAGE_SIZE) {
                lastItem = result.documents[PAGE_SIZE.toInt()]
            }
            Log.e("RecipeRemoteDataSource", "query: $query - result : ${result.documents.size}")
            return PagedRecipes(
                key = lastItem,
                data = result.documents.map { snap ->
                    val data = snap.data
                    Recipe.fromFirebaseMap(snap.id, data)
                }
            )
        } catch (e: Exception) {
            Log.e("RecipeRemoteDataSource", e.message ?: "")
            return PagedRecipes(error = e.message)
        }
    }

    override suspend fun loadRecipeDetail(recipeId: String): Recipe {
        return try {
            val result = firestore.collection(RECIPES).document(recipeId).get().await()
            val data = result.data
            Recipe.fromFirebaseMap(result.id, data)
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun uploadRecipePhoto(pathToFile: Uri): String {
        val result = suspendCoroutine<String> { continuation ->

            MediaManager.get().upload(pathToFile)
                .unsigned("sqy0n4kv")
                .callback(object : UploadCallback {
                    override fun onStart(requestId: String?) {

                    }

                    override fun onProgress(requestId: String?, bytes: Long, totalBytes: Long) {

                    }

                    override fun onSuccess(
                        requestId: String?,
                        resultData: MutableMap<Any?, Any?>?
                    ) {
                        Log.d("RecipeRemoteDataSource", "Success $requestId : $resultData")
                        continuation.resume(resultData?.get("secure_url") as String)
                    }

                    override fun onError(requestId: String?, error: ErrorInfo?) {
                        continuation.resumeWithException(IllegalStateException(error?.description))
                    }

                    override fun onReschedule(requestId: String?, error: ErrorInfo?) {

                    }

                })
                .preprocess(
                    ImagePreprocessChain.limitDimensionsChain(1000, 1000)
                        .addStep(DimensionsValidator(10, 10, 1000, 1000))
                        .saveWith(BitmapEncoder(BitmapEncoder.Format.PNG, 80))
                )
                .dispatch(context)
        }

        return result
    }


}