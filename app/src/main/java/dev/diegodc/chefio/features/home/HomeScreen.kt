package dev.diegodc.chefio.features.home

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.google.accompanist.pager.ExperimentalPagerApi
import dev.diegodc.chefio.R
import dev.diegodc.chefio.common.theme.*
import dev.diegodc.chefio.common.ui.LoadingContent
import dev.diegodc.chefio.models.Receipt
import dev.diegodc.chefio.models.UserProfile
import java.util.*

@OptIn(ExperimentalLifecycleComposeApi::class, ExperimentalPagerApi::class)
@Composable
fun HomeScreen(
    onReceiptClick: (Receipt) -> Unit,
    @StringRes userMessage: Int,
    onUserMessageDisplayed: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    LoadingContent(
        isLoading = uiState.value.isLoading,
        isEmpty = uiState.value.receipts.isEmpty() && !uiState.value.isLoading,
        emptyContent = { ReceiptEmptyContent(modifier) },
        onRefresh = {
            viewModel.loadData()
        }
    ) {
        Column(
            modifier = modifier
                .background(
                    color = FORM
                )
                .fillMaxSize()
                .padding(
                    horizontal = dimensionResource(id = R.dimen.horizontal_margin),
                )
        ) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 128.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(uiState.value.receipts) { receipt ->
                    ReceiptCardItem(
                        receipt = receipt,
                        onReceiptClick = onReceiptClick,
                        onReceiptFavorite = { },
                    )
                }
            }
        }
    }
}

@Composable
fun ReceiptEmptyContent(modifier: Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("No results :(")
    }
}

@Composable
fun ReceiptCardItem(
    receipt: Receipt,
    onReceiptClick: (Receipt) -> Unit,
    onReceiptFavorite: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,

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
                        model = receipt.creator.photo,
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
                    receipt.creator.username,
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
                AsyncImage(
                    model = receipt.image,
                    contentDescription = "Receipt image",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop,
                    error = painterResource(id = R.drawable.ic_baseline_person_24),
                    placeholder = painterResource(id = R.drawable.ic_baseline_person_24)
                )
            }
            Column {
                Text(
                    receipt.title,
                    style = MaterialTheme.typography.h2.copy(
                        color = MAIN_TEXT,
                    ),
                )
                Text(
                    when {
                        receipt.timeToPrepare == 1 -> {
                            "${receipt.timeToPrepare} min"
                        }
                        receipt.timeToPrepare <= 60 -> {
                            "${receipt.timeToPrepare} mins"
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

@Preview
@Composable
private fun previewReceiptCard() {
    ChefIOTheme {
        Surface(color = PRIMARY_COLOR) {
            ReceiptCardItem(
                receipt = Receipt(
                    id = "test",
                    title = "Test receipt",
                    timeToPrepare = 60,
                    image = "",
                    isFavorite = true,
                    creator = UserProfile(
                        name = "Lorem",
                        lastName = "Ipsum",
                        username = "lorem_ipsum",
                        photo = ""
                    ),
                    createdAt = Date()
                ),
                modifier = Modifier.padding(8.dp),
                onReceiptClick = { },
                onReceiptFavorite = { }
            )
        }
    }
}