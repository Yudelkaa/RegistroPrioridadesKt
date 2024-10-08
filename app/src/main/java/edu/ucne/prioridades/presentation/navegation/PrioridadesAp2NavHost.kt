package edu.ucne.prioridades.presentation.navegation


import androidx.compose.material3.DrawerState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import edu.ucne.prioridades.data.repository.PrioridadRepository
import edu.ucne.prioridades.data.repository.SistemaRepository
import edu.ucne.prioridades.data.repository.TicketRepository
import edu.ucne.prioridades.presentation.prioridad.PrioridadListScreen
import edu.ucne.prioridades.presentation.sistema.SistemaListScreen
import edu.ucne.prioridades.presentation.prioridad.PrioridadScreen
import edu.ucne.prioridades.presentation.sistema.SistemaScreen
import edu.ucne.prioridades.presentation.ticket.TicketListScreen
import edu.ucne.prioridades.presentation.ticket.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Composable
fun PrioridadesAp2NavHost(

    navHost: NavHostController,
    drawerState: DrawerState,
    scope: CoroutineScope,
    prioridadRepository: PrioridadRepository,
    ticketRepository: TicketRepository,
    sistemaRepository: SistemaRepository


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
                openDrawer = {
                    scope.launch {
                        drawerState.open()
                    }
                },
                onNavigateToPrioridades = { navHost.navigate(Screen.PrioridadesListScreen) },
                onNavigateToTickets = { navHost.navigate(Screen.TicketListScreen) },
                onNavigateToSistemas = {navHost.navigate(Screen.SistemaListScreen)},
                prioridadRepository = prioridadRepository,
                ticketRepository = ticketRepository
            )
        }

        composable<Screen.PrioridadScreen> {
            val args = it.toRoute<Screen.PrioridadScreen>()
            PrioridadScreen(
                goBack = {
                    navHost.navigateUp()
                },
                inicialPrioridadId = args.prioridadId,
                onNavigateToPrioridades = { args.prioridadId },
                onNavigateToTickets = { args.prioridadId },
                onNavigateToSistemas = { args.prioridadId},
                openDrawer = {
                    scope.launch {
                        drawerState.open()
                    }
                },
                prioridadId = args.prioridadId
            )
        }

        composable<Screen.TicketListScreen> {
           TicketListScreen(
               goToTicket = {
                   navHost.navigate(Screen.TicketScreen(it))
               },
               openDrawer = {
                   scope.launch {
                       drawerState.open()
                   }
               },
               onNavigateToPrioridades = { navHost.navigate(Screen.PrioridadesListScreen) },
               onNavigateToTickets = { navHost.navigate(Screen.TicketListScreen) },
               onNavigateToSistemas = {navHost.navigate(Screen.SistemaListScreen)},
               prioridadRepository = prioridadRepository,
               ticketRepository = ticketRepository,
               sistemaRepository = sistemaRepository

           )
        }

        composable<Screen.TicketScreen> {
            val args = it.toRoute<Screen.TicketScreen>()
            TicketScreen(
                ticketId = args.ticketId,
                goBack = {
                    navHost.navigateUp()
                },
                openDrawer = {
                    scope.launch {
                        drawerState.open()
                    }
                }
            )
        }
        composable<Screen.SistemaListScreen>{
            SistemaListScreen(
                onEdit = {navHost.navigate(Screen.SistemaScreen(it))},
                onAdd = {navHost.navigate(Screen.SistemaScreen(0))},
                onVerSistema = {navHost.navigate(Screen.SistemaScreen(it.id ?: 0))},
                openDrawer = {
                    scope.launch {
                        drawerState.open()
                    }
                },

            )

        }
        composable<Screen.SistemaScreen>{
            val args = it.toRoute<Screen.SistemaScreen>()
            SistemaScreen(
                goSistemaList = {
                    navHost.navigate(
                        Screen.SistemaListScreen
                    )
                },
                goBack = {
                    navHost.navigateUp()
                },
                id = args.id,
                openDrawer = {
                    scope.launch {
                        drawerState.open()
                    }
                }
            )

        }
    }
}

