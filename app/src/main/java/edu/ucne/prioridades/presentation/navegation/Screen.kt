package edu.ucne.prioridades.presentation.navegation
import androidx.navigation.NavBackStackEntry
import kotlinx.serialization.Serializable

sealed class Screen{

    @Serializable
    data object PrioridadesListScreen : Screen() //Consulta Prioridades
    @Serializable
    data class PrioridadScreen(val prioridadId : Int) : Screen() //Registro Prioridades
    @Serializable
    data object TicketListScreen : Screen() //Consulta Ticket
    @Serializable
    data class TicketScreen(val ticketId : Int) : Screen() //Registro Ticket

    @Serializable
    data object SistemaListScreen : Screen() //Consulta Sistema
    @Serializable
    data class SistemaScreen(val id: Int) : Screen() //Registro Sistema

}
