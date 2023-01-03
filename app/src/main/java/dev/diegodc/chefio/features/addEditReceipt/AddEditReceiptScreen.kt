package dev.diegodc.chefio.features.addEditReceipt

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import dev.diegodc.chefio.R
import dev.diegodc.chefio.common.theme.*
import dev.diegodc.chefio.common.ui.MainButton
import dev.diegodc.chefio.common.ui.OutlineMainButton
import dev.diegodc.chefio.common.ui.TextMainButton
import dev.diegodc.chefio.common.utils.disabledHorizontalPointerInputScroll
import dev.diegodc.chefio.features.addEditReceipt.models.AddEditReceiptState

@OptIn(ExperimentalLifecycleComposeApi::class, ExperimentalPagerApi::class)
@Composable
fun AddEditReceiptScreen(
    onReceiptUpdate: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AddEditReceiptViewModel = hiltViewModel()
) {
    val pagerState = rememberPagerState()

    Scaffold(
        modifier = modifier,
    ) {

        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        Column(modifier = Modifier.padding(it)) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextMainButton(
                    label = "Cancel",
                    modifier = Modifier,
                    onClick = onBack,
                    color = SECONDARY_COLOR
                )
                Text(
                    "${uiState.step}/${uiState.totalStep}",
                    style = MaterialTheme.typography.h2,
                    color = MAIN_TEXT
                )
            }
            HorizontalPager(
                count = uiState.totalStep,
                state = pagerState,
                modifier = Modifier
                    .weight(weight = 1f, fill = false)
                    .disabledHorizontalPointerInputScroll(disabled = true),
            ) { page ->
                when (page) {
                    0 -> {
                        AddEditStepOne(
                            modifier = Modifier,
                            state = uiState,
                            onTitleChange = viewModel::updateTitle,
                            onDescriptionChange = viewModel::updateDescription,
                            onTimeChange = viewModel::updateTimeToPrepare
                        )
                    }
                    1 -> {
                        AddEditStepTwo(
                            modifier = Modifier,
                            state = uiState,
                            onIngredientAdded = viewModel::addNewIngredient,
                            onPrepareStepAdded = viewModel::addNewPreparationStep,
                            onIngredientModify = viewModel::updateIngredient,
                            onStepPreparationModify = viewModel::updateStepPreparation
                        )
                    }
                }
            }

            Box {
                MainButton(
                    label = "Next",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    onClick = {
                        if (uiState.step < uiState.totalStep) {
                            viewModel.goToNextPage()
                        } else {
                            viewModel.saveReceipt()
                        }
                    },
                    enabled = uiState.isAvailableNextStep
                )
            }
        }

        LaunchedEffect(uiState.step) {
            pagerState.animateScrollToPage(page = uiState.step - 1)
        }

        LaunchedEffect(uiState.isSaved) {
            if (uiState.isSaved) {
                onReceiptUpdate()
            }
        }
    }
}

@Composable
fun AddEditStepOne(
    modifier: Modifier = Modifier,
    state: AddEditReceiptState,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onTimeChange: (Float) -> Unit
) {
    Column(
        modifier = modifier.padding(
            start = 24.dp,
            end = 24.dp
        )
    ) {
        Box(
            modifier = Modifier
                .padding(top = 32.dp)
                .height(160.dp)
                .fillMaxWidth()
                .border(
                    border = BorderStroke(width = 2.dp, color = OUTLINE),
                    shape = RoundedCornerShape(16.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_image_upload),
                    contentDescription = "",
                    modifier = Modifier.size(64.dp)
                )
                Text(
                    text = stringResource(R.string.upload_cover_photo),
                    style = MaterialTheme.typography.h3,
                    color = MAIN_TEXT
                )
            }
        }

        Text(
            stringResource(R.string.food_name),
            style = MaterialTheme.typography.h2,
            color = MAIN_TEXT,
            modifier = Modifier.padding(top = 24.dp)
        )
        OutlinedTextField(
            value = state.title,
            onValueChange = onTitleChange,
            singleLine = true,
            textStyle = MaterialTheme.typography.body1,
            shape = RoundedCornerShape(32.dp),
            placeholder = { Text("Enter food name") },
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = OUTLINE,
                placeholderColor = SECONDARY_TEXT,
                textColor = MAIN_TEXT
            )
        )
        Text(
            stringResource(R.string.food_description),
            style = MaterialTheme.typography.h2,
            color = MAIN_TEXT,
            modifier = Modifier.padding(top = 24.dp)
        )
        OutlinedTextField(
            value = state.description,
            onValueChange = onDescriptionChange,
            textStyle = MaterialTheme.typography.body1,
            shape = RoundedCornerShape(8.dp),
            placeholder = { Text("Tell a little about your food") },
            modifier = Modifier
                .height(88.dp)
                .padding(top = 8.dp)
                .fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = OUTLINE,
                placeholderColor = SECONDARY_TEXT,
                textColor = MAIN_TEXT
            )
        )
        Text(
            stringResource(R.string.food_cooking_duration),
            style = MaterialTheme.typography.h2,
            color = MAIN_TEXT,
            modifier = Modifier.padding(top = 24.dp)
        )
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text("<10", style = MaterialTheme.typography.h3, color = PRIMARY_COLOR)
            Text(state.timeToPrepared.toInt().toString(), style = MaterialTheme.typography.h3, color = PRIMARY_COLOR)
            Text(">60", style = MaterialTheme.typography.h3, color = SECONDARY_TEXT)
        }
        Slider(
            value = state.timeToPrepared,
            onValueChange = onTimeChange,
            valueRange = 0f..60f,
        )
    }
}

@Composable
fun AddEditStepTwo(
    modifier: Modifier = Modifier,
    state: AddEditReceiptState,
    onIngredientAdded: () -> Unit,
    onPrepareStepAdded: () -> Unit,
    onIngredientModify: (Int, String) -> Unit,
    onStepPreparationModify: (Int, String) -> Unit
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(color = WHITE),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Row(
                modifier =
                Modifier.padding(
                    bottom = 8.dp,
                    top = 12.dp,
                    start = 16.dp,
                    end = 16.dp
                )
            ) {
                Text(
                    text = stringResource(id = R.string.receipt_ingredients),
                    style = MaterialTheme.typography.h2,
                    color = MAIN_TEXT
                )
            }
        }

        itemsIndexed(state.ingredients) { index, ingredient ->
            OutlinedTextField(
                value = ingredient,
                onValueChange = {
                              onIngredientModify.invoke(index, it)
                },
                singleLine = true,
                textStyle = MaterialTheme.typography.body1,
                shape = RoundedCornerShape(32.dp),
                placeholder = { Text("Enter ingredient") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = OUTLINE,
                    placeholderColor = SECONDARY_TEXT,
                    textColor = MAIN_TEXT
                )
            )
        }

        item {
            OutlineMainButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 8.dp,
                        bottom = 12.dp,
                        start = 16.dp,
                        end = 16.dp
                    ),
                color = OUTLINE,
                label = "Add ingredient",
                onClick = onIngredientAdded
            )
        }

        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(16.dp)
                    .background(color = FORM)
            )
        }

        item {
            Row(
                modifier = Modifier.padding(
                    bottom = 8.dp,
                    top = 12.dp,
                    start = 16.dp,
                    end = 16.dp,
                )
            ) {
                Text(
                    text = stringResource(id = R.string.receipt_steps),
                    style = MaterialTheme.typography.h2,
                    color = MAIN_TEXT
                )
            }
        }

        itemsIndexed(state.preparationSteps) { index, preparation ->
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

                OutlinedTextField(
                    value = preparation,
                    onValueChange = {
                                    onStepPreparationModify.invoke(index, it)
                    },
                    textStyle = MaterialTheme.typography.body1,
                    shape = RoundedCornerShape(8.dp),
                    placeholder = { Text("Tell a little about your food") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        unfocusedBorderColor = OUTLINE,
                        placeholderColor = SECONDARY_TEXT,
                        textColor = MAIN_TEXT
                    )
                )
            }
        }

        item {
            OutlineMainButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 8.dp, bottom = 12.dp, start = 16.dp,
                        end = 16.dp
                    ),
                color = OUTLINE,
                label = "Add step",
                onClick = onPrepareStepAdded
            )
        }
    }
}

@Preview
@Composable
private fun previewStepOne() {
    ChefIOTheme {
        Surface {
            AddEditStepOne(
                state = AddEditReceiptState(),
                onDescriptionChange = {},
                onTimeChange = {},
                onTitleChange = {},
            )
        }
    }
}

@Preview
@Composable
private fun previewStepTwo() {
    ChefIOTheme {
        Surface {
            AddEditStepTwo(
                state = AddEditReceiptState(),
                onIngredientAdded = {},
                onPrepareStepAdded = {},
                onIngredientModify = {v1,v2 ->},
                onStepPreparationModify = {v1,v2 ->}
            )
        }
    }
}