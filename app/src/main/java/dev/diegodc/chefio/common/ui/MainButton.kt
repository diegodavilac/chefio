package dev.diegodc.chefio.common.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.diegodc.chefio.common.theme.ChefIOTheme
import dev.diegodc.chefio.common.theme.PRIMARY_COLOR
import dev.diegodc.chefio.common.theme.fonts


@Composable
fun MainButton(
    modifier: Modifier,
    label: String = "",
    onClick: () -> Unit = {},
    enabled: Boolean = true
) {
    Button(
        enabled = enabled,
        modifier = modifier,
        shape = RoundedCornerShape(32.dp),
        contentPadding = PaddingValues(
            horizontal = 16.dp,
            vertical = 12.dp
        ),
        content = {
            Text(
                text = label,
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontSize = 15.sp,
                    lineHeight = 18.sp,
                    fontFamily = fonts,
                    fontWeight = FontWeight.Bold
                )
            )
        },
        onClick = onClick
    )
}

@Composable
fun OutlineMainButton(modifier: Modifier, label: String = "", onClick: () -> Unit = {}) {
    OutlinedButton(
        modifier = modifier,
        shape = RoundedCornerShape(32.dp),
        border = BorderStroke(2.dp, PRIMARY_COLOR),
        contentPadding = PaddingValues(
            horizontal = 16.dp,
            vertical = 12.dp
        ),
        content = {
            Text(
                text = label,
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontSize = 15.sp,
                    lineHeight = 18.sp,
                    fontFamily = fonts,
                    fontWeight = FontWeight.Bold,
                    color = PRIMARY_COLOR
                )
            )
        },
        onClick = onClick
    )
}

@Composable
fun TextMainButton(
    modifier: Modifier,
    label: String = "",
    color: Color = PRIMARY_COLOR,
    onClick: () -> Unit = {}
) {
    TextButton(
        modifier = modifier,
        contentPadding = PaddingValues(
            horizontal = 16.dp,
            vertical = 12.dp
        ),
        shape = RoundedCornerShape(32.dp),
        content = {
            Text(
                text = label,
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontSize = 15.sp,
                    lineHeight = 18.sp,
                    fontFamily = fonts,
                    fontWeight = FontWeight.Bold,
                    color = color
                )
            )
        },
        onClick = onClick
    )
}


@Preview
@Composable
private fun buttonsPreview() {
    ChefIOTheme {
        Surface(
            color = Color.White
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MainButton(
                    label = "Default",
                    modifier = Modifier.fillMaxWidth(),
                )
                OutlineMainButton(
                    label = "Default",
                    modifier = Modifier.fillMaxWidth(),
                )
                TextMainButton(
                    label = "Default",
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}