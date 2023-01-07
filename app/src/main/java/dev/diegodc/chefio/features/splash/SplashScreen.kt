package dev.diegodc.chefio.features.splash

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.diegodc.chefio.R
import dev.diegodc.chefio.common.theme.MAIN_TEXT
import dev.diegodc.chefio.common.theme.SECONDARY_TEXT
import dev.diegodc.chefio.common.ui.MainButton
import kotlinx.coroutines.delay


@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    onGetStarted: () -> Unit,
    onUserAuthenticated: () -> Unit,
    viewModel: SplashViewModel = hiltViewModel()
) {

    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = true) {
        delay(3000L)
        viewModel.updateLoading(false)
    }

    LaunchedEffect(key1 = uiState.value.isLoading, key2 = uiState.value.isAuthenticated) {
        if (uiState.value.isLoading.not() && uiState.value.isAuthenticated) {
            onUserAuthenticated()
        }
    }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(weight = 1f, fill = true),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.onboarding),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth()
            )
        }

        AnimatedVisibility(
            visible = uiState.value.isAuthenticated.not() && uiState.value.isLoading.not()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    stringResource(R.string.splash_title),
                    style = MaterialTheme.typography.h1,
                    color = MAIN_TEXT,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(
                        start = 24.dp,
                        end = 24.dp,
                        top = 16.dp,
                        bottom = 8.dp
                    )
                )

                Text(
                    stringResource(R.string.splash_body),
                    style = MaterialTheme.typography.body1,
                    color = SECONDARY_TEXT,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(
                        horizontal = 48.dp
                    )
                )

                MainButton(
                    modifier = Modifier.fillMaxWidth().padding(
                        horizontal = 24.dp,
                        vertical = 32.dp
                    ),
                    label = stringResource(R.string.get_started),
                    onClick = {
                        onGetStarted()
                    },

                    )
            }
        }
    }
}