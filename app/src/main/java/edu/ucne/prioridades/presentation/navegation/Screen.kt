package edu.ucne.prioridades.presentation.navegation
import kotlinx.serialization.Serializable


sealed class Screen{
    @Serializable
    data object PrioridadesListScreen : Screen() //Consulta
    @Serializable
    data class PrioridadScreen(val prioridadId : Int) : Screen() //Registro
    companion object
}
