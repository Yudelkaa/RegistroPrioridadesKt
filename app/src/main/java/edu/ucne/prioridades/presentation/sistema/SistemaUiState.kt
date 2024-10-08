package edu.ucne.prioridades.presentation.sistema

import edu.ucne.prioridades.data.remote.dto.SistemaDto

data class SistemaUiState(
    val id: Int? = null,
    var nombre: String? = "",
    var nombreError: String? = null,
    val errorMessage: String? = null,
    val sistemas: List<SistemaDto> = emptyList(),
    val isLoading: Boolean = false
    )
