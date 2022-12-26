package dev.diegodc.chefio

import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dev.diegodc.chefio.common.theme.*
import dev.diegodc.chefio.features.home.HomeScreen
import dev.diegodc.chefio.features.home.TabScreen
import kotlinx.coroutines.CoroutineScope

@Composable
fun HomeNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    startDestination: String = Destinations.HOME_ROUTE,
    navActions: NavigationActions = remember(navController) {
        NavigationActions(navController)
    }
) {
    val currentNavBackStackEntry by navController.currentBackStackEntryAsState()
    val items = listOf(
        TabScreen.Home,
        TabScreen.MapList,
    )

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navActions.navigateToAddEditReceipt(R.string.add_receipt, null)
                },
                backgroundColor = PRIMARY_COLOR,
                contentColor = WHITE,
            ) {
                Icon(painter = painterResource(R.drawable.ic_edit), contentDescription = "")
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        bottomBar = {
            BottomNavigation(
                backgroundColor = WHITE,
                contentColor = SECONDARY_TEXT
            ) {
                val currentDestination = currentNavBackStackEntry?.destination

                items.forEach { screen ->
                    val isSelected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
                    BottomNavigationItem(
                        icon = { Icon(painter = painterResource(screen.iconId), contentDescription = null, tint = if (isSelected) PRIMARY_COLOR else SECONDARY_TEXT) },
                        label = { Text(stringResource(screen.resourceId), color = if (isSelected) PRIMARY_COLOR else SECONDARY_TEXT) },
                        selected = isSelected,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = modifier.padding(innerPadding)
        ) {
            composable(
                Destinations.HOME_ROUTE,
                arguments = listOf(
                    navArgument(DestinationsArgs.USER_MESSAGE_ARG) {
                        type = NavType.IntType; defaultValue = 0
                    }
                )
            ) { entry ->
                HomeScreen(
                    onReceiptClick = { receipt -> navActions.navigateToReceiptDetail(receipt.id) },
                    onUserMessageDisplayed = {
                        entry.arguments?.putInt(
                            DestinationsArgs.USER_MESSAGE_ARG,
                            0
                        )
                    },
                    userMessage = entry.arguments?.getInt(DestinationsArgs.USER_MESSAGE_ARG)!!,
                    modifier = Modifier
                )
            }
            composable(Destinations.MAP_ROUTE) {
                Surface(color = PRIMARY_COLOR, modifier = Modifier.fillMaxSize()) { }
            }
        }
    }
}