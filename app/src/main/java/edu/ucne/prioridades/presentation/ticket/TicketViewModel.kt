package edu.ucne.prioridades.presentation.ticket

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import edu.ucne.prioridades.data.repository.TicketRepository
import edu.ucne.prioridades.data.local.entities.TicketEntity
import edu.ucne.prioridades.data.repository.PrioridadRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class TicketViewModel @Inject constructor(
    private val ticketRepository: TicketRepository,
    private val prioridadRepository: PrioridadRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TicketUiState())
    val uiState = _uiState.asStateFlow()

    private var ticketId: Int? = null

    fun setTicketId(id: Int?) {
        if (id == null || id == 0) {
            // Reset UI state for a new ticket
            _uiState.update {
                it.copy(
                    ticketId = 0,
                    descripcion = "",
                    asunto = "",
                    cliente = "",
                    fecha = LocalDate.now(),
                    prioridadId = 0
                )
            }
        } else {
            ticketId = id
            loadTicket()
        }
    }

    fun loadTicket() {
        viewModelScope.launch {
            val ticket = ticketId?.let { ticketRepository.getTicket(it) }
            ticket?.let {
                _uiState.update { currentUiState ->
                    currentUiState.copy(
                        ticketId = it.ticketId,
                        fecha = LocalDate.parse(it.fecha, DateTimeFormatter.ofPattern("MM/dd/yyyy")),
                        prioridadId = it.prioridadId ?: 0,
                        descripcion = it.descripcion,
                        asunto = it.asunto,
                        cliente = it.cliente
                    )
                }
            }
        }
    }

    fun onDescripcionChange(descripcion: String) {
        _uiState.update { it.copy(descripcion = descripcion) }
    }

    fun onAsuntoChange(asunto: String) {
        _uiState.update { it.copy(asunto = asunto) }
    }

    fun onClienteChange(cliente: String) {
        _uiState.update { it.copy(cliente = cliente) }
    }

    fun onFechaChange(fecha: LocalDate) {
        _uiState.update { it.copy(fecha = fecha) }
    }

    val prioridad = prioridadRepository.getPrioridades()
        .stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            emptyList()
        )

    val ticket = ticketRepository.getTickets()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun validateTicket(): Boolean {
        val currentState = _uiState.value
        var isValid = true
        if (currentState.descripcion.isBlank()) {
            _uiState.update { it.copy(descripcionError = "La descripción no puede estar vacía") }
            isValid = false
        }
        if (currentState.asunto.isBlank()) {
            _uiState.update { it.copy(asuntoError = "El asunto no puede estar vacío") }
            isValid = false
        }
        if (currentState.cliente.isBlank()) {
            _uiState.update { it.copy(clienteError = "El cliente no puede estar vacío") }
            isValid = false
        }
        return isValid
    }

    // Método para borrar un ticket
    fun delete(ticketId: Int) {
        viewModelScope.launch {
            val currentTicketId = _uiState.value.ticketId
            if (currentTicketId != 0) {
                val ticket = ticketRepository.getTicket(currentTicketId)
                ticket?.let {
                    ticketRepository.deleteTicket(it)
                    _uiState.update { it.copy(errorMessage = null) } // Reset error message
                }
            } else {
                _uiState.update { it.copy(errorMessage = "ID de ticket no válido") }
            }
        }
    }


    fun save() {
        viewModelScope.launch {
            val descripcion = _uiState.value.descripcion
            val asunto = _uiState.value.asunto
            val cliente = _uiState.value.cliente
            val prioridadId = _uiState.value.prioridadId

            if (!validateTicket()) return@launch


            val isExisting = ticketRepository.findByDescripcion(descripcion)
            if (isExisting && (_uiState.value.ticketId == 0)) {
                _uiState.update { it.copy(errorMessage = "Ya existe un ticket con esta descripción") }
                return@launch
            }


            ticketRepository.save(_uiState.value.toEntity())
            _uiState.update { it.copy(errorMessage = null) }
        }
    }

    data class TicketUiState(
        val ticketId: Int = 0,
        val descripcion: String = "",
        val asunto: String = "",
        val cliente: String = "",
        var fecha: LocalDate = LocalDate.now(),
        val descripcionError: String? = null,
        val asuntoError: String? = null,
        val clienteError: String? = null,
        val tickets: List<TicketEntity> = emptyList(),
        val prioridadId: Int = 0,
        val errorMessage: String? = null
    ) {
        val hasError: Boolean
            get() = descripcionError != null || asuntoError != null || clienteError != null
    }

    fun TicketUiState.toEntity(): TicketEntity {
        return TicketEntity(
            ticketId = ticketId,
            fecha = fecha.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")),
            descripcion = descripcion,
            asunto = asunto,
            cliente = cliente,
            prioridadId = prioridadId
        )
    }
}
