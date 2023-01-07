package dev.diegodc.chefio.features.signUp

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.diegodc.chefio.R
import dev.diegodc.chefio.common.theme.*
import dev.diegodc.chefio.common.ui.BulletText
import dev.diegodc.chefio.common.ui.ChefioLoading
import dev.diegodc.chefio.common.ui.MainButton

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    viewModel: SignUpViewModel = hiltViewModel(),
    onUserAuthenticated: () -> Unit
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    SignUpContent(
        modifier = modifier
            .background(color = FORM)
            .fillMaxSize(),
        uiState = uiState,
        onTogglePassword = {
            viewModel.updateShowPassword(uiState.value.showPassword.not())
        },
        onPasswordChanged = viewModel::updatePassword,
        onEmailChanged = viewModel::updateEmail,
        onSignUp = viewModel::signUp
    )

    ChefioLoading( show = uiState.value.isLoading)

    LaunchedEffect(uiState.value.isAuthenticated) {
        if (uiState.value.isAuthenticated) {
            onUserAuthenticated()
        }
    }

}

@Composable
fun SignUpContent(
    modifier: Modifier = Modifier,
    uiState: State<SignUpViewState>,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onTogglePassword: () -> Unit,
    onSignUp: () -> Unit
) {
    Column(
        modifier = modifier
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            stringResource(R.string.sign_in_title),
            style = MaterialTheme.typography.h1,
            color = MAIN_TEXT,
            textAlign = TextAlign.Center,
        )
        Text(
            stringResource(R.string.sign_in_body),
            style = MaterialTheme.typography.body2,
            color = SECONDARY_TEXT,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(
                bottom = 16.dp
            )
        )

        OutlinedTextField(
            value = uiState.value.email,
            onValueChange = onEmailChanged,
            singleLine = true,
            textStyle = MaterialTheme.typography.body1,
            shape = RoundedCornerShape(32.dp),
            placeholder = { Text("Email", style = MaterialTheme.typography.body1) },
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = OUTLINE,
                placeholderColor = SECONDARY_TEXT,
                textColor = MAIN_TEXT,
                leadingIconColor = MAIN_TEXT,
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            ),
            leadingIcon = {
                Icon(
                    painterResource(R.drawable.ic_email),
                    contentDescription = "",
                    modifier = Modifier.size(24.dp),
                )
            }
        )

        OutlinedTextField(
            value = uiState.value.password,
            onValueChange = onPasswordChanged,
            singleLine = true,
            visualTransformation = if (uiState.value.showPassword) VisualTransformation.None else PasswordVisualTransformation(),
            textStyle = MaterialTheme.typography.body1,
            shape = RoundedCornerShape(32.dp),
            placeholder = { Text("Password", style = MaterialTheme.typography.body1) },
            modifier = Modifier
                .padding(top = 12.dp)
                .fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = OUTLINE,
                placeholderColor = SECONDARY_TEXT,
                textColor = MAIN_TEXT,
                leadingIconColor = MAIN_TEXT,
            ),
            leadingIcon = {
                Icon(
                    painterResource(R.drawable.ic_lock),
                    contentDescription = "",
                    modifier = Modifier.size(24.dp),
                )
            },
            trailingIcon = {
                Icon(
                    painterResource(R.drawable.ic_eye),
                    contentDescription = "",
                    modifier = Modifier.size(24.dp).clip(
                        RoundedCornerShape(16.dp)
                    ).clickable {
                        onTogglePassword()
                    },
                    tint = if (uiState.value.showPassword) PRIMARY_COLOR else SECONDARY_TEXT
                )
            }
        )

        Text(
            stringResource(R.string.sign_up_rules_title),
            style = MaterialTheme.typography.body1,
            color = MAIN_TEXT,
            modifier = Modifier.fillMaxWidth().padding(top = 24.dp),
        )

        BulletText(
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
            label = stringResource(R.string.sign_up_rule_one),
            selected = uiState.value.atLeast6Chars
        )

        BulletText(
            modifier = Modifier.fillMaxWidth(),
            label = stringResource(R.string.sign_up_rule_two),
            selected = uiState.value.containsNumber
        )

        MainButton(
            modifier = Modifier.fillMaxWidth().padding(top = 32.dp),
            label = stringResource(R.string.sign_up),
            onClick = onSignUp,
        )
    }
}