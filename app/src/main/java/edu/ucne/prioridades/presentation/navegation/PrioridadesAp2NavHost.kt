package edu.ucne.prioridades.presentation.navegation

import PrioridadScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import edu.ucne.prioridades.presentation.prioridad.PrioridadListScreen


@Composable
fun PrioridadesAp2NavHost(
    navHost: NavHostController
) {
    NavHost(
        navController = navHost,
        startDestination = Screen.PrioridadesListScreen
    ) {
        composable<Screen.PrioridadesListScreen> {
            PrioridadListScreen(
                goToPrioridad = {
                    navHost.navigate(Screen.PrioridadScreen(it))
                },
                goToAddPrioridad = {
                    navHost.navigate(Screen.PrioridadScreen(0))
                }
            )
        }

        composable<Screen.PrioridadScreen> {
            val args = it.toRoute<Screen.PrioridadScreen>()
            PrioridadScreen(
                goBack = {
                    navHost.navigateUp()
                },
                inicialPrioridadId = args.prioridadId
            )
        }
    }
}
