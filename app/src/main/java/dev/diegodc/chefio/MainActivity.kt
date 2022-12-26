package dev.diegodc.chefio

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import dev.diegodc.chefio.common.theme.ChefIOTheme

@AndroidEntryPoint
class MainActivity  : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChefIOTheme {
                MainNavGraph()
            }
        }
    }
}