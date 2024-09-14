package edu.ucne.prioridades.presentation.prioridad

sealed class PrioridadUiState {
    data class DescriptionChange(val descripcion: String) : PrioridadUiState()
    data class DaysChange(val diasCompromiso: Int) : PrioridadUiState()
    data class PrioridadIdChange(val prioridadId: Int) : PrioridadUiState()
    data class SelectedPrioridad(val prioridadId: Int) : PrioridadUiState()
    object NewPrioridad : PrioridadUiState()
    object save : PrioridadUiState()
    object Delete : PrioridadUiState()

}