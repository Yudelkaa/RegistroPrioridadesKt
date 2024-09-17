package edu.ucne.prioridades

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.*
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import edu.ucne.prioridades.presentation.NavigationDrawer
import edu.ucne.prioridades.presentation.navegation.PrioridadesAp2NavHost
import edu.ucne.prioridades.presentation.navegation.Screen
import edu.ucne.prioridades.ui.theme.PrioridadesTheme
import edu.ucne.prioridades.data.repository.PrioridadRepository
import edu.ucne.prioridades.data.repository.TicketRepository
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

     @Inject lateinit var prioridadRepository: PrioridadRepository
    @Inject lateinit var ticketRepository: TicketRepository


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PrioridadesTheme {
                val navHost = rememberNavController()
                val scope = rememberCoroutineScope()
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

                NavigationDrawer(
                    navPrioridadList = { navHost.navigate(Screen.PrioridadesListScreen) },
                    navTicketList = { navHost.navigate(Screen.TicketListScreen) },
                    drawerState = drawerState
                ) {
                    PrioridadesAp2NavHost(
                        navHost,
                        prioridadRepository = prioridadRepository,
                        scope = scope,
                        drawerState = drawerState,
                        ticketRepository = ticketRepository

                    )
                }
            }
        }
    }
}

//
//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun Preview() {
//    val prioridadList = listOf(
//        PrioridadEntity(1, "Enel", 9),
//        PrioridadEntity(2, "Juan", 9)
//    )
//
//
//    PrioridadListScreen(
//        goToPrioridad = { },
//        openDrawer = {},
//        goToAddPrioridad = {}
//    )
//}
//
//
//
//




