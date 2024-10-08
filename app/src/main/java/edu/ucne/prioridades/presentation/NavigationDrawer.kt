package edu.ucne.prioridades.presentation

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

data class NavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    var badgeCount: Double? = null
)

@Composable
fun NavigationDrawer(
    drawerState: DrawerState,
    navPrioridadList: () -> Unit,
    navTicketList: () -> Unit,
    navSistemaList: () -> Unit,
    content: @Composable () -> Unit
) {
    val scope = rememberCoroutineScope()
    val items = listOf(
        NavigationItem(
            title = "Prioridades",
            selectedIcon = Icons.Filled.Star,
            unselectedIcon = Icons.Outlined.Star
        ),
        NavigationItem(
            title = "Tickets",
            selectedIcon = Icons.Filled.Email,
            unselectedIcon = Icons.Outlined.Email
        ),
        NavigationItem(
            title = "Sistemas",
            selectedIcon = Icons.Filled.Favorite,
            unselectedIcon = Icons.Outlined.Favorite
        ),

    )
    val selectedItem = remember { mutableStateOf(items[0]) }

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Menu :D", modifier = Modifier.padding(16.dp))
                HorizontalDivider()
                items.forEach { item ->
                    NavigationDrawerItem(
                        icon = {
                            Icon(
                                imageVector = if (item == selectedItem.value) {
                                    item.selectedIcon
                                } else item.unselectedIcon,
                                contentDescription = item.title
                            )
                        },
                        label = {
                            Text(text = item.title)
                        },
                        selected = item == selectedItem.value,
                        onClick = {
                            selectedItem.value = item
                            scope.launch { drawerState.close() }
                            when (item.title) {
                                "Prioridades" -> navPrioridadList()
                                "Tickets" -> navTicketList()
                                "Sistemas" -> navSistemaList()

                            }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
            }
        },
        drawerState = drawerState
    ) {
        content()
    }
}
