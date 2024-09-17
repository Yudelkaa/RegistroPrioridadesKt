package edu.ucne.prioridades.presentation.navegation
import kotlinx.serialization.Serializable
import java.time.LocalDate

sealed class Screen{

    @Serializable
    data object PrioridadesListScreen : Screen() //Consulta
    @Serializable
    data class PrioridadScreen(val prioridadId : Int) : Screen() //Registro
    @Serializable
    data object TicketListScreen : Screen() //Consulta Ticket,
    @Serializable
    data class TicketScreen(val ticketId : Int) : Screen() //Registro Ticket

}
