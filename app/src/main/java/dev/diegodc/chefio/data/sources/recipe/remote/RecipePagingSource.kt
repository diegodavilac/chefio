package dev.diegodc.chefio.data.sources.recipe.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.firebase.firestore.DocumentSnapshot
import dev.diegodc.chefio.data.sources.recipe.RecipeDataSource
import dev.diegodc.chefio.models.Recipe

class RecipePagingSource(
    private val remoteDataSource: RecipeDataSource,
    private val query : String? = null
) : PagingSource<DocumentSnapshot, Recipe>() {

    override fun getRefreshKey(state: PagingState<DocumentSnapshot, Recipe>): DocumentSnapshot? {

        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.nextKey ?: anchorPage?.prevKey
        }
    }

    override suspend fun load(params: LoadParams<DocumentSnapshot>): LoadResult<DocumentSnapshot, Recipe> {
        return try {
            // Start refresh at page 1 if undefined.
            val nextPageNumber = params.key
            val response = remoteDataSource.loadRecipesFromFirebase(nextPageNumber, query)
            LoadResult.Page(
                data = response.data,
                prevKey = null, // Only paging forward.
                nextKey = response.key
            )
        } catch (e: Exception) {
            // Handle errors in this block and return LoadResult.Error if it is an
            // expected error (such as a network failure).
            LoadResult.Error(e)
        }
    }
}