package dev.diegodc.chefio.features.receiptDetails

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.google.android.gms.maps.model.LatLng
import dev.diegodc.chefio.R
import dev.diegodc.chefio.common.theme.*
import dev.diegodc.chefio.common.ui.BulletText
import dev.diegodc.chefio.common.ui.ChefioLoading
import dev.diegodc.chefio.models.Recipe

@OptIn(ExperimentalMaterialApi::class, ExperimentalLifecycleComposeApi::class)
@Composable
fun RecipeDetailScreen(
    onBackPressed: () -> Unit,
    viewModel: RecipeDetailViewModel = hiltViewModel(),
    toLocation: (LatLng) -> Unit
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp

    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState()

    val radius = if (bottomSheetScaffoldState.bottomSheetState.isCollapsed) 32.dp else 0.dp
    val cornerRadius: Dp by animateDpAsState(radius)

    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        sheetContent = {
            if (uiState.value.recipe != null) {
                RecipeDetailContent(
                    modifier = Modifier.height(screenHeight.dp),
                    recipe = uiState.value.recipe!!,
                    toLocation = toLocation
                )
            }
        },
        sheetShape = RoundedCornerShape(
            topEnd = cornerRadius, topStart = cornerRadius,
        ),
        sheetPeekHeight = (screenHeight * 0.70).dp
    ) {
        Box(
            modifier = Modifier.height((screenHeight * 0.35).dp),
            contentAlignment = Alignment.TopStart
        ) {

            AsyncImage(
                model = uiState.value.recipe?.image,
                contentDescription = "Recipe cover photo",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height((screenHeight * 0.35).dp),
                error = painterResource(id = R.drawable.recipe_placeholder_background),
                placeholder = painterResource(id = R.drawable.recipe_placeholder_background)
            )

            Icon(
                painter = painterResource(R.drawable.ic_round_arrow_back),
                tint = Color.White,
                modifier = Modifier.padding(12.dp)
                    .size(40.dp)
                    .background(color = BULLET_GREY.copy(alpha = 0.5f), shape = CircleShape)
                    .clip(CircleShape)
                    .clickable {
                        onBackPressed()
                    }.padding(8.dp),
                contentDescription = "",
            )
        }
    }

    ChefioLoading(
        show = uiState.value.isLoading
    )
}

@Composable
fun RecipeDetailContent(
    modifier: Modifier = Modifier,
    recipe: Recipe,
    toLocation: (LatLng) -> Unit
) {
    LazyColumn(
        modifier = modifier.background(
            color = Color.White,
        ).fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(recipe.title, style = MaterialTheme.typography.h2, color = MAIN_TEXT)
        }
        item {
            Row {
                Text(
                    "Food - ${recipe.timeToPrepare} mins",
                    style = MaterialTheme.typography.body2,
                    color = SECONDARY_TEXT
                )
                Box(modifier = Modifier.weight(1f))
                Row(
                    modifier = Modifier.clickable {
                        if (recipe.location != null)
                            toLocation(recipe.location)
                    },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Show location",
                        style = MaterialTheme.typography.body2,
                        color = SECONDARY_TEXT,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Icon(
                        painter = painterResource(R.drawable.map_marker_radius),
                        contentDescription = "",
                        modifier = Modifier.size(24.dp),
                        tint = PRIMARY_COLOR
                    )
                }
            }
        }
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AsyncImage(
                    model = "recipe.creator.photo",
                    contentDescription = "Receipt creator profile image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(color = FORM, shape = CircleShape),
                    error = painterResource(id = R.drawable.ic_baseline_person_24),
                    placeholder = painterResource(id = R.drawable.ic_baseline_person_24)
                )
                Text(recipe.creator.name, style = MaterialTheme.typography.h3, color = MAIN_TEXT)
            }
        }
        item {
            Box(
                modifier = Modifier.height(1.dp).fillMaxWidth().background(color = OUTLINE)
            )
        }
        item {
            Text(
                stringResource(R.string.recipe_description),
                style = MaterialTheme.typography.h2,
                color = MAIN_TEXT
            )
        }
        item {
            Text(
                recipe.description,
                style = MaterialTheme.typography.body2,
                color = SECONDARY_TEXT
            )
        }
        item {
            Box(
                modifier = Modifier.height(1.dp).fillMaxWidth().background(color = OUTLINE)
            )
        }
        item {
            Text(
                stringResource(R.string.recipe_ingredients),
                style = MaterialTheme.typography.h2,
                color = MAIN_TEXT
            )
        }

        itemsIndexed(recipe.ingredients) { index, item ->
            BulletText(
                modifier = Modifier.fillMaxWidth(),
                label = item,
                selected = true
            )
        }

        item {
            Box(
                modifier = Modifier.height(1.dp).fillMaxWidth().background(color = OUTLINE)
            )
        }

        item {
            Text(
                stringResource(R.string.recipe_steps),
                style = MaterialTheme.typography.h2,
                color = MAIN_TEXT
            )
        }

        itemsIndexed(recipe.preparationSteps) { index, step ->
            Row(
                modifier = Modifier.padding(
                    horizontal = 16.dp
                )
            ) {
                Box(
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(24.dp)
                        .background(
                            color = MAIN_TEXT,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${index + 1}",
                        style = TextStyle(
                            fontFamily = fonts,
                            fontWeight = FontWeight.Bold,
                            color = WHITE,
                            fontSize = 12.sp
                        ),
                        textAlign = TextAlign.Center,
                    )
                }
                Text(
                    step.description,
                    style = MaterialTheme.typography.body2,
                    color = MAIN_TEXT,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        }
    }
}