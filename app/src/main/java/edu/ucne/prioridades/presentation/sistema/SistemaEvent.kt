package edu.ucne.prioridades.presentation.sistema

sealed interface SistemaEvent {
    data class NombreChanged(val nombre: String) : SistemaEvent
    data class SistemaSelected(val id: Int) : SistemaEvent

    data object Guardar : SistemaEvent
    data class Borrar (val id: Int): SistemaEvent
    data object Nuevo : SistemaEvent
    data object Validate: SistemaEvent
    data class ObtenerSistema(val id: Int) : SistemaEvent

}