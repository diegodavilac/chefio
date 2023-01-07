@file:OptIn(ExperimentalMaterialApi::class)

package dev.diegodc.chefio.features.home

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.accompanist.pager.ExperimentalPagerApi
import dev.diegodc.chefio.R
import dev.diegodc.chefio.common.theme.*
import dev.diegodc.chefio.common.ui.TextMainButton
import dev.diegodc.chefio.features.home.models.HomeViewState
import dev.diegodc.chefio.models.Recipe
import dev.diegodc.chefio.models.UserProfile
import java.util.*

@OptIn(ExperimentalLifecycleComposeApi::class, ExperimentalPagerApi::class)
@Composable
fun HomeScreen(
    onRecipeClick: (Recipe) -> Unit,
    @StringRes userMessage: Int,
    onUserMessageDisplayed: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val uiRecipes = viewModel.recipesPager.collectAsLazyPagingItems()

    HomeContent(
        modifier = modifier,
        uiRecipes = uiRecipes,
        onRecipeClick = onRecipeClick,
        onSearchChange = viewModel::updateQuery,
        state = uiState
    )
}

@Composable
fun HomeContent(
    modifier: Modifier = Modifier,
    uiRecipes: LazyPagingItems<Recipe>,
    onRecipeClick: (Recipe) -> Unit,
    onSearchChange: (String) -> Unit,
    state: State<HomeViewState>
) {

    if (uiRecipes.loadState.refresh is LoadState.Loading) {
        CircularProgressIndicator(
            modifier = Modifier.fillMaxSize(),
        )
    }

    Column(
        modifier = modifier
            .background(
                color = FORM
            )
            .fillMaxSize()
    ) {

        OutlinedTextField(
            value = state.value.query,
            onValueChange = onSearchChange,
            singleLine = true,
            textStyle = MaterialTheme.typography.body1,
            shape = RoundedCornerShape(32.dp),
            placeholder = { Text("Enter recipe name") },
            modifier = Modifier
                .padding(top = 32.dp, start = 24.dp, end = 24.dp)
                .fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = OUTLINE,
                placeholderColor = SECONDARY_TEXT,
                textColor = MAIN_TEXT
            ),
//            TODO: add selector to search by title or ingredient
//            leadingIcon = {
//
//            },
            trailingIcon = {
                Icon(
                    painterResource(R.drawable.ic_round_search_24),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = SECONDARY_TEXT
                )
            }
        )

        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 128.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 16.dp, horizontal = 16.dp)
        ) {
            items(
                items = uiRecipes.itemSnapshotList,
                key = { recipes ->
                    recipes?.id ?: ""
                }
            ) { recipe ->
                if (recipe != null) {
                    RecipeCardItem(
                        recipe = recipe,
                        onRecipeClick = onRecipeClick,
                        onRecipeFavorite = { },
                    )
                }
            }
        }
    }
}

@Composable
fun RecipeEmptyContent(modifier: Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("No results :(")
    }
}

@Composable
fun RecipeCardItem(
    recipe: Recipe,
    onRecipeClick: (Recipe) -> Unit,
    onRecipeFavorite: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        onClick = {
            onRecipeClick(recipe)
        },
        backgroundColor = WHITE
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(
                            color = OUTLINE,
                            shape = RoundedCornerShape(11.dp)
                        ),
                ) {
                    AsyncImage(
                        model = recipe.creator.photo,
                        contentDescription = "Receipt creator profile image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(11.dp)),
                        error = painterResource(id = R.drawable.ic_baseline_person_24),
                        placeholder = painterResource(id = R.drawable.ic_baseline_person_24)
                    )
                }
                Text(
                    recipe.creator.username,
                    style = MaterialTheme.typography.subtitle1.copy(
                        color = MAIN_TEXT,
                    ),
                    modifier = Modifier
                        .padding(start = 8.dp)
                )
            }
            Box(
                modifier = Modifier
                    .size(144.dp)
                    .background(
                        color = OUTLINE,
                        shape = RoundedCornerShape(16.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                val test: String = recipe.image
                AsyncImage(
                    model = test,
                    contentDescription = "Receipt image",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop,
                    error = painterResource(id = R.drawable.recipe_placeholder_background),
                    placeholder = painterResource(id = R.drawable.recipe_placeholder_background)
                )
            }
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    recipe.title,
                    style = MaterialTheme.typography.h2.copy(
                        color = MAIN_TEXT,
                    ),
                )
                Text(
                    when {
                        recipe.timeToPrepare == 1 -> {
                            "${recipe.timeToPrepare} min"
                        }
                        recipe.timeToPrepare <= 60 -> {
                            "${recipe.timeToPrepare} mins"
                        }
                        else -> {
                            ">60 mins"
                        }
                    },
                    style = MaterialTheme.typography.subtitle1.copy(
                        color = SECONDARY_TEXT,
                    ),
                )
            }
        }
    }
}