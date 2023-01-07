package dev.diegodc.chefio.features.addEditRecipe

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import dev.diegodc.chefio.R
import dev.diegodc.chefio.common.theme.*
import dev.diegodc.chefio.common.ui.ChefioLoading
import dev.diegodc.chefio.common.ui.MainButton
import dev.diegodc.chefio.common.ui.OutlineMainButton
import dev.diegodc.chefio.common.ui.TextMainButton
import dev.diegodc.chefio.common.utils.disabledHorizontalPointerInputScroll
import dev.diegodc.chefio.features.addEditRecipe.models.AddEditRecipeState

@OptIn(ExperimentalLifecycleComposeApi::class, ExperimentalPagerApi::class)
@Composable
fun AddEditRecipeScreen(
    onRecipeUpdate: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AddEditRecipeViewModel = hiltViewModel()
) {
    val pagerState = rememberPagerState()
    val pickMediaLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                Log.d("PhotoPicker", "Selected URI: $uri")
                viewModel.updatePhotoCover(uri)
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val initialLocation = LatLng(1.35, 103.87)
    val cameraPositionState: CameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(initialLocation, 11f)
    }

    Scaffold(
        modifier = modifier,
        backgroundColor = WHITE
    ) {

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
                            onTimeChange = viewModel::updateTimeToPrepare,
                            onImagePicker = {
                                pickMediaLauncher.launch(
                                    PickVisualMediaRequest(
                                        ActivityResultContracts.PickVisualMedia.ImageOnly
                                    )
                                )
                            },
                            onAddressChange = viewModel::onSearchChanged,
                            cameraState = cameraPositionState
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
                            viewModel.saveRecipe()
                        }
                    },
                    enabled = uiState.isAvailableNextStep
                )
            }
        }
    }

    ChefioLoading(
        show = uiState.isLoading
    )

    LaunchedEffect(uiState.step) {
        pagerState.animateScrollToPage(page = uiState.step - 1)
    }

    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) {
            onRecipeUpdate()
        }
    }

    LaunchedEffect(key1 = uiState.location) {
        if (uiState.location != null) {
            val position = LatLng(uiState.location!!.latitude, uiState.location!!.longitude)
            cameraPositionState.position = CameraPosition.fromLatLngZoom(position, 11f)
            cameraPositionState.move(CameraUpdateFactory.zoomIn())
        }
    }

    LaunchedEffect(cameraPositionState.position) {
        val position = cameraPositionState.position
        viewModel.onMarkerChanged(position.target.latitude, position.target.longitude)
    }
}

@Composable
fun AddEditStepOne(
    modifier: Modifier = Modifier,
    state: AddEditRecipeState,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onTimeChange: (Float) -> Unit,
    onImagePicker: () -> Unit,
    onAddressChange: (String) -> Unit,
    cameraState: CameraPositionState
) {
    Column(
        modifier = modifier.padding(
            start = 24.dp,
            end = 24.dp
        ).verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .padding(top = 32.dp)
                .height(160.dp)
                .fillMaxWidth()
                .border(
                    border = BorderStroke(width = 2.dp, color = OUTLINE),
                    shape = RoundedCornerShape(16.dp)
                )
                .clip(RoundedCornerShape(16.dp))
                .clickable {
                    onImagePicker()
                },
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
            if (state.photoPath != null) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(state.photoPath)
                        .build(),
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
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
            Text(
                state.timeToPrepared.toInt().toString(),
                style = MaterialTheme.typography.h3,
                color = PRIMARY_COLOR
            )
            Text(">60", style = MaterialTheme.typography.h3, color = SECONDARY_TEXT)
        }
        Slider(
            value = state.timeToPrepared,
            onValueChange = onTimeChange,
            valueRange = 0f..60f,
        )
        Text(
            stringResource(R.string.recipe_location),
            style = MaterialTheme.typography.h2,
            color = MAIN_TEXT,
            modifier = Modifier.padding(top = 24.dp)
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {
            OutlinedTextField(
                value = state.address,
                onValueChange = onAddressChange,
                singleLine = true,
                textStyle = MaterialTheme.typography.body1,
                shape = RoundedCornerShape(32.dp),
                placeholder = { Text("Enter food address") },
                modifier = Modifier
                    .padding(top = 8.dp, bottom = 16.dp)
                    .fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = OUTLINE,
                    placeholderColor = SECONDARY_TEXT,
                    textColor = MAIN_TEXT
                )
            )
            Box(modifier = Modifier.height(400.dp)) {
                GoogleMap(
                    modifier = Modifier.matchParentSize(),
                    properties = MapProperties(),
                    uiSettings = MapUiSettings(
                        indoorLevelPickerEnabled = false,
                        rotationGesturesEnabled = false,
                    ),
                    cameraPositionState = cameraState
                )
                MapPinOverlay()
            }
        }
    }
}

@Composable
fun MapPinOverlay(){
    Column(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.BottomCenter
        ){
            Icon(
                modifier = Modifier.size(50.dp),
                painter = painterResource(R.drawable.ic_baseline_location),
                tint = PRIMARY_COLOR,
                contentDescription = "Pin Image"
            )
        }
        Box(
            Modifier.weight(1f)
        ){}
    }
}

@Composable
fun AddEditStepTwo(
    modifier: Modifier = Modifier,
    state: AddEditRecipeState,
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
                    text = stringResource(id = R.string.recipe_ingredients),
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
                    text = stringResource(id = R.string.recipe_steps),
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