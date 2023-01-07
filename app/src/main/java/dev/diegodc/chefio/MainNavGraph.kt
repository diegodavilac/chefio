package dev.diegodc.chefio

import android.app.Activity
import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.maps.model.LatLng
import dev.diegodc.chefio.DestinationsArgs.MAP_LAT_ARG
import dev.diegodc.chefio.DestinationsArgs.MAP_LNG_ARG
import dev.diegodc.chefio.DestinationsArgs.RECIPE_ID_ARG
import dev.diegodc.chefio.DestinationsArgs.TITLE_ARG
import dev.diegodc.chefio.DestinationsArgs.USER_MESSAGE_ARG
import dev.diegodc.chefio.features.addEditRecipe.AddEditRecipeScreen
import dev.diegodc.chefio.features.map.RecipeMapScreen
import dev.diegodc.chefio.features.receiptDetails.RecipeDetailScreen
import dev.diegodc.chefio.features.signIn.SignInScreen
import dev.diegodc.chefio.features.signUp.SignUpScreen
import dev.diegodc.chefio.features.splash.SplashScreen
import kotlinx.coroutines.CoroutineScope

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MainNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    startDestination: String = Destinations.SPLASH_ROUTE,
    navActions: NavigationActions = remember(navController) {
        NavigationActions(navController)
    }
) {
    val currentNavBackStackEntry by navController.currentBackStackEntryAsState()
    val permissionState = rememberMultiplePermissionsState(
        listOf(android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION)
    )

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(route = Destinations.SPLASH_ROUTE) {
            SplashScreen(
                onGetStarted = {
                    navActions.navigateToSignIn()
                },
                onUserAuthenticated = {
                    navActions.navigateToDashboard()
                }
            )
        }
        composable(route = Destinations.SIGN_IN_ROUTE) {
            SignInScreen(
                onUserAuthenticated = {
                    navActions.navigateToDashboard()
                },
                coroutineScope = coroutineScope,
                onSignUp = {
                    navActions.navigateToSignUp()
                }
            )
        }
        composable(route = Destinations.SIGN_UP_ROUTE) {
            SignUpScreen(
                onUserAuthenticated = {
                    navActions.navigateToDashboard()
                },
            )
        }
        composable(
            Destinations.DASHBOARD_ROUTE,
            arguments = listOf(
                navArgument(USER_MESSAGE_ARG) { type = NavType.IntType; defaultValue = 0 }
            )
        ) { _ ->
            HomeNavGraph(
                navActions = navActions,
                coroutineScope = coroutineScope,
            )
        }
        composable(
            Destinations.ADD_EDIT_RECIPE_ROUTE,
            arguments = listOf(
                navArgument(TITLE_ARG) { type = NavType.IntType },
                navArgument(RECIPE_ID_ARG) { type = NavType.StringType; nullable = true },
            )
        ) { entry ->
            val recipeId = entry.arguments?.getString(RECIPE_ID_ARG)
            AddEditRecipeScreen(
                onRecipeUpdate = {
                    navActions.navigateToDashboard(clearBackStack = false)
                },
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            Destinations.RECIPE_DETAIL_ROUTE,
            arguments = listOf(
                navArgument(RECIPE_ID_ARG) { type = NavType.StringType },
            )
        ) {
            RecipeDetailScreen(
                onBackPressed = {
                    navController.popBackStack()
                },
                toLocation = {
                    navActions.navigateToRecipeMap(it)
                }
            )
        }
        composable(
            Destinations.MAP_ROUTE,
            arguments = listOf(
                navArgument(MAP_LAT_ARG) { type = NavType.FloatType },
                navArgument(MAP_LNG_ARG) { type = NavType.FloatType }
            )
        ) { entry ->
            val lat = entry.arguments?.getFloat(MAP_LAT_ARG)?:0f
            val lng = entry.arguments?.getFloat(MAP_LNG_ARG)?:0f

            RecipeMapScreen(
                onBackPressed = {
                    navController.popBackStack()
                },
                position = LatLng(lat.toDouble(),lng.toDouble())
            )
        }
    }

    LaunchedEffect(permissionState.permissions) {
        if (permissionState.permissions.any { it.hasPermission.not() }){
            permissionState.launchMultiplePermissionRequest()
        }
    }
}


// Keys for navigation
const val ADD_EDIT_RESULT_OK = Activity.RESULT_FIRST_USER + 1
const val DELETE_RESULT_OK = Activity.RESULT_FIRST_USER + 2
const val EDIT_RESULT_OK = Activity.RESULT_FIRST_USER + 3