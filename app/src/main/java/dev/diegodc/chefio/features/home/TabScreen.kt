package dev.diegodc.chefio.features.home

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import dev.diegodc.chefio.R

sealed class TabScreen(val route: String, @StringRes val resourceId: Int, @DrawableRes val iconId: Int) {
    object Home : TabScreen("home", R.string.home, R.drawable.ic_home)
    object MapList : TabScreen("map", R.string.map, R.drawable.ic_home)
}