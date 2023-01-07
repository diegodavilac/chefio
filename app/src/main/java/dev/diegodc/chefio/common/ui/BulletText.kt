package dev.diegodc.chefio.common.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import dev.diegodc.chefio.R
import dev.diegodc.chefio.common.theme.*

@Composable
fun BulletText(
    modifier: Modifier = Modifier,
    label: String,
    selected: Boolean = false,
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {

        Box(
            modifier = Modifier.size(24.dp)
                .background(
                    color = if (selected) PRIMARY_ACCENT else BULLET_GREY.copy(alpha = 0.2f),
                    shape = CircleShape,
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_round_check_24),
                contentDescription = "",
                tint = if (selected) PRIMARY_COLOR else SECONDARY_TEXT,
                modifier = Modifier.size(16.dp)
            )
        }

        Text(
            label,
            style = MaterialTheme.typography.body2,
            color = if (selected) MAIN_TEXT else SECONDARY_TEXT,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}