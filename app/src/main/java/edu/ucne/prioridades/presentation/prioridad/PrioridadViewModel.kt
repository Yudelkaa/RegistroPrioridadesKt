package edu.ucne.prioridades.presentation.prioridad

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.prioridades.data.repository.PrioridadRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import edu.ucne.prioridades.data.local.entities.PrioridadEntity
import javax.inject.Inject

@HiltViewModel
class PrioridadViewModel @Inject constructor(
    private val prioridadRepository: PrioridadRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    init {
        getPrioridades()
    }

    fun onEvent(event: PrioridadUiState) {
        when(event) {
            is PrioridadUiState.Delete -> delete()
            is PrioridadUiState.DescriptionChange -> onDescriptionChange(event.descripcion)
            is PrioridadUiState.DaysChange -> onDaysChange(event.diasCompromiso)
            is PrioridadUiState.save -> save()
            is PrioridadUiState.NewPrioridad -> nuevo()
            is PrioridadUiState.SelectedPrioridad -> selectedPrioridad(event.prioridadId)
            is PrioridadUiState.PrioridadIdChange -> onPrioridadIdChange(event.prioridadId)
        }
    }

    fun save() {
        viewModelScope.launch {
            val descripcion = _uiState.value.descripcion
            val diasCompromiso = _uiState.value.diasCompromiso

            if (descripcion.isNullOrBlank()) {
                _uiState.update {
                    it.copy(errorMessage = "La descripción no puede estar vacía")
                }
                return@launch
            }

            if (diasCompromiso == null || diasCompromiso <= 0) {
                _uiState.update {
                    it.copy(errorMessage = "Los días de compromiso deben ser mayores a cero")
                }
                return@launch
            }

            val isExisting = prioridadRepository.findByDescripcion(descripcion)
            if (isExisting && (_uiState.value.prioridadId == null || _uiState.value.prioridadId == 0)) {
                _uiState.update {
                    it.copy(errorMessage = "Ya existe una prioridad con esta descripción")
                }
                return@launch
            }

            prioridadRepository.save(_uiState.value.toEntity())
            _uiState.update { it.copy(errorMessage = null) }
        }
    }

    fun delete() {
        viewModelScope.launch {
            if (_uiState.value.prioridadId != null) {
                prioridadRepository.delete(_uiState.value.toEntity())
                getPrioridades()
            } else {
                _uiState.update {
                    it.copy(errorMessage = "ID de prioridad no válido")
                }
            }
        }
    }

    fun nuevo() {
        _uiState.update {
            it.copy(
                prioridadId = null,
                descripcion = "",
                diasCompromiso = null,
                errorMessage = null
            )
        }
    }

    fun selectedPrioridad(prioridadId: Int) {
        viewModelScope.launch {
            if (prioridadId > 0) {
                val prioridad = prioridadRepository.getPrioridad(prioridadId)

                _uiState.update {
                    it.copy(
                        prioridadId = prioridad?.prioridadId,
                        descripcion = prioridad?.descripcion,
                        diasCompromiso = prioridad?.diasCompromiso
                    )
                }
            }
        }
    }

    private fun getPrioridades() {
        viewModelScope.launch {
            prioridadRepository.getPrioridades().collect { prioridades ->
                _uiState.update {
                    it.copy(prioridades = prioridades)
                }
            }
        }
    }

    fun onDescriptionChange(descripcion: String?) {
        _uiState.update {
            it.copy(descripcion = descripcion)
        }
    }

    fun onDaysChange(diasCompromiso: Int?) {
        _uiState.update {
            it.copy(diasCompromiso = diasCompromiso)
        }
    }

    fun onPrioridadIdChange(prioridadId: Int) {
        _uiState.update {
            it.copy(prioridadId = prioridadId)
        }
    }
}

data class UiState(
    val prioridadId: Int? = null,
    val descripcion: String? = "",
    val diasCompromiso: Int? = null,
    val errorMessage: String? = null,
    val prioridades: List<PrioridadEntity> = emptyList()
)

fun UiState.toEntity() = PrioridadEntity(
    prioridadId = prioridadId,
    descripcion = descripcion ?: "",
    diasCompromiso = diasCompromiso
)
