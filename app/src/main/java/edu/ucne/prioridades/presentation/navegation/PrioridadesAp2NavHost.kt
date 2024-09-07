package edu.ucne.prioridades.presentation.navegation

import androidx.compose.runtime.Composable
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import edu.ucne.prioridades.presentation.prioridad.PrioridadListScreen
import edu.ucne.prioridades.data.local.database.PrioridadDb
import edu.ucne.prioridades.presentation.prioridad.PrioridadScreen

@Composable
fun PrioridadesAp2NavHost(
    navHost: NavHostController,
    prioridadDb: PrioridadDb
) {
    NavHost(
        navController = navHost,
        startDestination = Screen.PrioridadesListScreen
    ) {
        composable<Screen.PrioridadesListScreen> {
            PrioridadListScreen(
                prioridadDb.prioridadDao().getAll()
                    .collectAsStateWithLifecycle(
                        initialValue = emptyList(),
                        lifecycleOwner = LocalLifecycleOwner.current,
                        minActiveState = Lifecycle.State.STARTED
                    ).value,
                goToPrioridad = {
                    navHost.navigate(Screen.PrioridadScreen(it))
                },
                goToAddPrioridad = {
                    navHost.navigate(Screen.PrioridadScreen(0))
                },
                onEditPrioridad = {
                    navHost.navigate(Screen.PrioridadScreen(0))
                },
                onDeletePrioridad = { navHost.navigate(Screen.PrioridadScreen(0)) },
            )
        }

        composable<Screen.PrioridadScreen> {
            val args = it.toRoute<Screen.PrioridadScreen>()
            PrioridadScreen(
                prioridadId = args.prioridadId,
                goBack = {
                    navHost.navigateUp()
                },
                prioridadDb = prioridadDb
            )
        }
    }
}