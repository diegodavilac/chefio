package dev.diegodc.chefio.common.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun ChefioLoading(
    show : Boolean = false
) {
    AnimatedVisibility(
        visible = show,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box (
            modifier = Modifier.fillMaxSize().background(color = Color.Black.copy(alpha = 0.35f)),
            contentAlignment = Alignment.Center
        ){
            CircularProgressIndicator()
        }
    }
}