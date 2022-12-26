package dev.diegodc.chefio

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import dev.diegodc.chefio.Destinations.HOME_ROUTE
import dev.diegodc.chefio.Destinations.RECEIPT_DETAIL_ROUTE
import dev.diegodc.chefio.DestinationsArgs.RECEIPT_ID_ARG
import dev.diegodc.chefio.DestinationsArgs.TITLE_ARG
import dev.diegodc.chefio.DestinationsArgs.USER_MESSAGE_ARG
import dev.diegodc.chefio.Screens.ADD_EDIT_RECEIPT_SCREEN
import dev.diegodc.chefio.Screens.DASHBOARD_SCREEN
import dev.diegodc.chefio.Screens.HOME_SCREEN
import dev.diegodc.chefio.Screens.MAP_SCREEN
import dev.diegodc.chefio.Screens.RECEIPT_DETAIL_SCREEN

/**
 * Screens used in [Destinations]
 */
private object Screens {
    const val DASHBOARD_SCREEN = "dashboard"
    const val HOME_SCREEN = "home"
    const val MAP_SCREEN = "map"
    const val RECEIPT_DETAIL_SCREEN = "receipt"
    const val ADD_EDIT_RECEIPT_SCREEN = "addEditReceipt"
}

/**
 * Arguments used in [Destinations] routes
 */
object DestinationsArgs {
    const val USER_MESSAGE_ARG = "userMessage"
    const val RECEIPT_ID_ARG = "taskId"
    const val TITLE_ARG = "title"
}

/**
 * Destinations used in the [MainActivity]
 */
object Destinations {
    const val DASHBOARD_ROUTE ="$DASHBOARD_SCREEN?$USER_MESSAGE_ARG={$USER_MESSAGE_ARG}"
    const val HOME_ROUTE = HOME_SCREEN
    const val MAP_ROUTE = MAP_SCREEN
    const val RECEIPT_DETAIL_ROUTE = "$RECEIPT_DETAIL_SCREEN/{$RECEIPT_ID_ARG}"
    const val ADD_EDIT_RECEIPT_ROUTE = "$ADD_EDIT_RECEIPT_SCREEN/{$TITLE_ARG}?$RECEIPT_ID_ARG={$RECEIPT_ID_ARG}"
}

/**
 * Models the navigation actions in the app.
 */
class NavigationActions(private val navController: NavHostController) {

    fun navigateToDashboard(resultCode : Int){
        navController.navigate(Destinations.DASHBOARD_ROUTE) {
            // Pop up to the start destination of the graph to
            // avoid building up a large stack of destinations
            // on the back stack as users select items
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            // Avoid multiple copies of the same destination when
            // reselecting the same item
            launchSingleTop = true
            // Restore state when reselecting a previously selected item
            restoreState = true
        }
    }

    fun navigateToReceiptDetail(taskId: String) {
        navController.navigate("$RECEIPT_DETAIL_ROUTE/$taskId")
    }

    fun navigateToAddEditReceipt(title: Int, taskId: String?) {
        navController.navigate(
            "$ADD_EDIT_RECEIPT_SCREEN/$title".let {
                if (taskId != null) "$it?$RECEIPT_ID_ARG=$taskId" else it
            }
        )
    }
}