package dev.diegodc.chefio

import android.util.Log
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.google.android.gms.maps.model.LatLng
import dev.diegodc.chefio.Destinations.MAP_ROUTE
import dev.diegodc.chefio.Destinations.RECIPE_DETAIL_ROUTE
import dev.diegodc.chefio.Destinations.SIGN_IN_ROUTE
import dev.diegodc.chefio.Destinations.SIGN_UP_ROUTE
import dev.diegodc.chefio.Destinations.SPLASH_ROUTE
import dev.diegodc.chefio.DestinationsArgs.MAP_LAT_ARG
import dev.diegodc.chefio.DestinationsArgs.MAP_LNG_ARG
import dev.diegodc.chefio.DestinationsArgs.RECIPE_ID_ARG
import dev.diegodc.chefio.DestinationsArgs.TITLE_ARG
import dev.diegodc.chefio.DestinationsArgs.USER_MESSAGE_ARG
import dev.diegodc.chefio.Screens.ADD_EDIT_RECIPE_SCREEN
import dev.diegodc.chefio.Screens.DASHBOARD_SCREEN
import dev.diegodc.chefio.Screens.HOME_SCREEN
import dev.diegodc.chefio.Screens.MAP_SCREEN
import dev.diegodc.chefio.Screens.PROFILE_SCREEN
import dev.diegodc.chefio.Screens.RECIPE_DETAIL_SCREEN
import dev.diegodc.chefio.Screens.SIGN_IN_SCREEN
import dev.diegodc.chefio.Screens.SIGN_UP_SCREEN
import dev.diegodc.chefio.Screens.SPLASH_SCREEN

/**
 * Screens used in [Destinations]
 */
private object Screens {
    const val DASHBOARD_SCREEN = "dashboard"
    const val HOME_SCREEN = "home"
    const val MAP_SCREEN = "map"
    const val PROFILE_SCREEN = "profile"
    const val RECIPE_DETAIL_SCREEN = "recipe"
    const val ADD_EDIT_RECIPE_SCREEN = "addEditRecipe"
    const val SPLASH_SCREEN = "splash"
    const val SIGN_IN_SCREEN = "signIn"
    const val SIGN_UP_SCREEN = "singUp"
}

/**
 * Arguments used in [Destinations] routes
 */
object DestinationsArgs {
    const val USER_MESSAGE_ARG = "userMessage"
    const val RECIPE_ID_ARG = "recipeId"
    const val TITLE_ARG = "title"
    const val MAP_LAT_ARG ="latitude"
    const val MAP_LNG_ARG = "longitude"
}

/**
 * Destinations used in the [MainActivity]
 */
object Destinations {
    const val DASHBOARD_ROUTE ="$DASHBOARD_SCREEN?$USER_MESSAGE_ARG={$USER_MESSAGE_ARG}"
    const val HOME_ROUTE = HOME_SCREEN
    const val MAP_ROUTE = "$MAP_SCREEN?$MAP_LAT_ARG={$MAP_LAT_ARG}&$MAP_LNG_ARG={$MAP_LNG_ARG}"
    const val RECIPE_DETAIL_ROUTE = "$RECIPE_DETAIL_SCREEN/{$RECIPE_ID_ARG}"
    const val ADD_EDIT_RECIPE_ROUTE = "$ADD_EDIT_RECIPE_SCREEN/{$TITLE_ARG}?$RECIPE_ID_ARG={$RECIPE_ID_ARG}"
    const val SPLASH_ROUTE = SPLASH_SCREEN
    const val SIGN_IN_ROUTE = SIGN_IN_SCREEN
    const val SIGN_UP_ROUTE = SIGN_UP_SCREEN
    const val PROFILE_ROUTE = PROFILE_SCREEN
}

/**
 * Models the navigation actions in the app.
 */
class NavigationActions(private val navController: NavHostController) {

    fun navigateToDashboard(clearBackStack: Boolean = true){
        navController.navigate(Destinations.DASHBOARD_ROUTE) {
            if (clearBackStack) {
                popUpTo(SPLASH_ROUTE){
                    inclusive = true
                }
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    fun navigateToRecipeDetail(recipeId: String) {
        navController.navigate("$RECIPE_DETAIL_SCREEN/$recipeId")
    }

    fun navigateToAddEditRecipe(title: Int, recipeId: String?) {
        navController.navigate(
            "$ADD_EDIT_RECIPE_SCREEN/$title".let {
                if (recipeId != null) "$it?$RECIPE_ID_ARG=$recipeId" else it
            }
        )
    }

    fun navigateToSignIn() {
        navController.navigate(
            SIGN_IN_ROUTE
        ) {
            popUpTo(SPLASH_ROUTE){
                inclusive = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    fun navigateToSignUp(){
        navController.navigate(SIGN_UP_ROUTE){
            launchSingleTop = true
            restoreState = true
        }
    }

    fun navigateToRecipeMap(position : LatLng) {
        navController.navigate(
            "$MAP_SCREEN?$MAP_LAT_ARG=${position.latitude.toFloat()}&$MAP_LNG_ARG=${position.longitude.toFloat()}"
        )
    }
}