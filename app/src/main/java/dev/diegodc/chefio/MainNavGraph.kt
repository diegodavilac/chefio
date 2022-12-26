package dev.diegodc.chefio

import android.app.Activity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavAction
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dev.diegodc.chefio.DestinationsArgs.RECEIPT_ID_ARG
import dev.diegodc.chefio.DestinationsArgs.TITLE_ARG
import dev.diegodc.chefio.DestinationsArgs.USER_MESSAGE_ARG
import dev.diegodc.chefio.common.theme.PRIMARY_COLOR
import dev.diegodc.chefio.features.addEditReceipt.AddEditReceiptScreen
import dev.diegodc.chefio.features.home.HomeScreen
import dev.diegodc.chefio.features.home.TabScreen
import kotlinx.coroutines.CoroutineScope

@Composable
fun MainNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    startDestination: String = Destinations.DASHBOARD_ROUTE,
    navActions: NavigationActions = remember(navController) {
        NavigationActions(navController)
    }
) {
    val currentNavBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentNavBackStackEntry?.destination?.route ?: startDestination
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
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
            Destinations.ADD_EDIT_RECEIPT_ROUTE,
            arguments = listOf(
                navArgument(TITLE_ARG) { type = NavType.IntType },
                navArgument(RECEIPT_ID_ARG) { type = NavType.StringType; nullable = true },
            )
        ) { entry ->
            val taskId = entry.arguments?.getString(RECEIPT_ID_ARG)
            AddEditReceiptScreen(
//                topBarTitle = entry.arguments?.getInt(TITLE_ARG)!!,
                onReceiptUpdate = {
                            navActions.navigateToDashboard(
                                if (taskId == null) ADD_EDIT_RESULT_OK else EDIT_RESULT_OK
                            )
                },
                onBack = {navController.popBackStack()}
            )
        }
        composable(Destinations.RECEIPT_DETAIL_ROUTE) {
//            TaskDetailScreen(
//                onEditTask = { taskId ->
//                    navActions.navigateToAddEditTask(R.string.edit_task, taskId)
//                },
//                onBack = { navController.popBackStack() },
//                onDeleteTask = { navActions.navigateToTasks(DELETE_RESULT_OK) }
//            )
        }
    }
}


// Keys for navigation
const val ADD_EDIT_RESULT_OK = Activity.RESULT_FIRST_USER + 1
const val DELETE_RESULT_OK = Activity.RESULT_FIRST_USER + 2
const val EDIT_RESULT_OK = Activity.RESULT_FIRST_USER + 3