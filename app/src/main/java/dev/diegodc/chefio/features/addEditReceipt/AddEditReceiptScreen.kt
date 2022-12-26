package dev.diegodc.chefio.features.addEditReceipt

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.diegodc.chefio.R
import dev.diegodc.chefio.common.theme.*
import dev.diegodc.chefio.common.ui.MainButton
import dev.diegodc.chefio.common.ui.TextMainButton
import dev.diegodc.chefio.features.addEditReceipt.models.AddEditReceiptState

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun AddEditReceiptScreen(
    onReceiptUpdate: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AddEditReceiptViewModel = hiltViewModel()
) {
    Scaffold(
        modifier = modifier,
    ) {

        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        Column(modifier = Modifier.padding(it)) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextMainButton(
                    label = "Cancel",
                    modifier = Modifier,
                    onClick = onBack,
                    color = SECONDARY_COLOR
                )
                Text("${uiState.step}/${uiState.totalStep}", style = MaterialTheme.typography.h2, color = MAIN_TEXT)
            }
            AddEditStepOne(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .weight(weight = 1f, fill = false),
                state = uiState,
                onTitleChange = viewModel::updateTitle,
                onDescriptionChange = viewModel::updateDescription,
                onTimeChange = viewModel::updateTimeToPrepare
            )
            Box {
                MainButton(
                    label = "Next",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    onClick = {},
                    enabled = viewModel.isAvailableNextStep()
                )
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
            modifier = Modifier.padding(top = 8.dp).fillMaxWidth(),
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
            modifier = Modifier.height(88.dp).padding(top = 8.dp).fillMaxWidth(),
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
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
        ) {
            Text("<10", style = MaterialTheme.typography.h3, color = PRIMARY_COLOR)
            Text("30", style = MaterialTheme.typography.h3, color = PRIMARY_COLOR)
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
fun AddEditStepOne(
    modifier: Modifier = Modifier,
    state: AddEditReceiptState,
    onIngredientAdded: () -> Unit,
    onPrepareStepAdded: () -> Unit,
){

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