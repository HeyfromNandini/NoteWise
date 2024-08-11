package project.app.notewise.baseUI.theme.bottomBar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import project.app.notewise.domain.models.BottomBarScreens
import project.app.notewise.domain.models.items

@Composable
fun BottomBar(navController: NavController, isBottomBarVisible: MutableState<Boolean>) {
    AnimatedVisibility(
        visible = isBottomBarVisible.value,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it })
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            elevation = CardDefaults.cardElevation(0.dp),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.background.copy(0.85f)
            )
        ) {
            NavigationBar(
                modifier = Modifier
                    .height(80.dp),
                containerColor = MaterialTheme.colorScheme.background,
                tonalElevation = 10.dp,
            ) {
                items.forEach {
                    val selected = isSelectedRoute(currentRoute?.route, it)
                    NavigationBarItem(
                        icon = {
                            AnimatedIcon(
                                iconSelected = if (selected) it.iconSelected else it.iconUnselected,
                                scale = if (selected) 1.2f else 1.2f,
                                color = MaterialTheme.colorScheme.onPrimary,
                            ) {
                                navController.navigate(it.t as Any) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                        selected = selected,
                        onClick = {
                            navController.navigate(it.t as Any) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                            indicatorColor = MaterialTheme.colorScheme.background,
                        ),
                        interactionSource = MutableInteractionSource()
                    )
                }
            }
        }
    }
}

fun isSelectedRoute(
    currentRoute: String?,
    screen: BottomBarScreens
): Boolean {
    val routeClassName = currentRoute?.substringAfterLast(".") ?: ""
    return routeClassName == screen.t?.toString()?.substringAfterLast(".")?.substringBefore("@")
}
