package dev.diegodc.chefio.features.signIn

import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dev.diegodc.chefio.R
import dev.diegodc.chefio.common.theme.*
import dev.diegodc.chefio.common.ui.ChefioLoading
import dev.diegodc.chefio.common.ui.MainButton
import dev.diegodc.chefio.common.ui.TextMainButton
import dev.diegodc.chefio.features.signIn.model.SignInViewState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    onUserAuthenticated: () -> Unit,
    viewModel: SignInViewModel = hiltViewModel(),
    coroutineScope: CoroutineScope,
    onSignUp: () -> Unit
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    SignInContent(
        modifier = modifier.fillMaxSize(),
        uiState = uiState,
        onSignUp = onSignUp,
        onEmailChanged = viewModel::updateEmail,
        onLogin = viewModel::login,
        onPasswordChanged = viewModel::updatePassword,
        onTogglePassword = {
            viewModel.updateShowPassword(uiState.value.showPass.not())
        }
    )

    ChefioLoading(uiState.value.isLoading)

    LaunchedEffect(uiState.value.isAuthenticated) {
        if (uiState.value.isAuthenticated) {
            onUserAuthenticated()
        }
    }

    LaunchedEffect(uiState.value.error) {
        if (uiState.value.error.isNullOrEmpty().not()) {
            //TODO: show error snackbar
        }
    }
}

@Composable
fun SignInContent(
    modifier: Modifier = Modifier,
    uiState: State<SignInViewState>,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onTogglePassword: () -> Unit,
    onLogin: () -> Unit,
    onSignUp: () -> Unit
) {
    Column(
        modifier = modifier.padding(horizontal = 24.dp),
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
            visualTransformation = if (uiState.value.showPass) VisualTransformation.None else PasswordVisualTransformation(),
            textStyle = MaterialTheme.typography.body1,
            shape = RoundedCornerShape(32.dp),
            placeholder = { Text("Password", style = MaterialTheme.typography.body1) },
            modifier = Modifier
                .padding(top = 8.dp)
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
                    tint = if (uiState.value.showPass) PRIMARY_COLOR else SECONDARY_TEXT
                )
            }
        )

        MainButton(
            modifier = Modifier.fillMaxWidth().padding(top = 32.dp),
            label = "Login",
            onClick = {
                onLogin()
            },
            enabled = uiState.value.credentialsCompleted
        )

        Text(
            stringResource(R.string.sign_in_options),
            style = MaterialTheme.typography.body2,
            color = SECONDARY_TEXT,
            modifier = Modifier.padding(
                top = 24.dp,
                bottom = 24.dp
            ),
            textAlign = TextAlign.Center
        )

        MainButton(
            modifier = Modifier.fillMaxWidth().padding(
                bottom = 24.dp
            ),
            label = "Google",
            color = SECONDARY_COLOR,
            icon = {
                Icon(
                    painter = painterResource(R.drawable.google_plus),
                    modifier = Modifier.size(24.dp),
                    contentDescription = "",
                    tint = Color.White
                )
            }
        )

        Row(
            modifier = Modifier.fillMaxWidth().padding(
                bottom = 24.dp
            ),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                stringResource(R.string.no_account_message),
                style = MaterialTheme.typography.body2,
                color = MAIN_TEXT,
            )

            TextMainButton(
                modifier = Modifier.padding(start = 12.dp),
                label = stringResource(R.string.sign_up),
                onClick = {
                    onSignUp()
                }
            )
        }
    }
}

@Composable
fun rememberFirebaseAuthLauncher(
    onAuthComplete: (AuthResult) -> Unit,
    onAuthError: (ApiException) -> Unit,
    coroutineScope: CoroutineScope
): ManagedActivityResultLauncher<Intent, ActivityResult> {

    return rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)!!
            val credential = GoogleAuthProvider.getCredential(account.idToken!!, null)
            coroutineScope.launch {
                val authResult = Firebase.auth.signInWithCredential(credential).await()
                onAuthComplete(authResult)
            }
        } catch (e: ApiException) {
            onAuthError(e)
        }
    }
}