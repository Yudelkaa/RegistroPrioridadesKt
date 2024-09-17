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

    private var ticketId: Int = 0

    fun setTicketId(id: Int) {
        ticketId = id
        if (ticketId > 0) {
            loadTicket()
        }
    }

    fun loadTicket() {
        viewModelScope.launch {
            val ticket = ticketRepository.getTicket(ticketId)
            ticket?.let {
                _uiState.update { currentUiState ->
                    currentUiState.copy(
                        ticketId = ticket.ticketId,
                        fecha = LocalDate.parse(ticket.fecha, DateTimeFormatter.ofPattern("MM/dd/yyyy")),
                        prioridadId = ticket.prioridadId,
                        descripcion = ticket.descripcion ?: "",
                        asunto = ticket.asunto ?: "",
                        cliente = ticket.cliente ?: ""
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
        _uiState.update {
            it.copy(fecha = fecha)
        }
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
        if (currentState.descripcion.isNullOrBlank()) {
            _uiState.update { it.copy(descripcionError = "La descripción no puede estar vacía") }
            isValid = false
        }
        if (currentState.asunto.isNullOrBlank()) {
            _uiState.update { it.copy(asuntoError = "El asunto no puede estar vacío") }
            isValid = false
        }
        if (currentState.cliente.isNullOrBlank()) {
            _uiState.update { it.copy(clienteError = "El cliente no puede estar vacío") }
            isValid = false
        }
        return isValid
    }



    fun deleteTicket() {
        viewModelScope.launch {
            ticketRepository.deleteTicket(TicketEntity(ticketId))
            _uiState.update { it.copy(ticketId = ticketId )}
        }
    }

    fun save() {
        viewModelScope.launch {
            ticketRepository.save(uiState.value.toEntity())
        }
    }


}
data class TicketUiState(
    val ticketId: Int? = 0,
    val descripcion: String? = null,
    val asunto: String? = null,
    val cliente: String? = null,
    var fecha: LocalDate = LocalDate.now(),
    val descripcionError: String? = null,
    val asuntoError: String? = null,
    val clienteError: String? = null,
    val tickets: List<TicketEntity> = emptyList(),
    val prioridadId: Int? = null
)

fun TicketUiState.toEntity(): TicketEntity {
    return TicketEntity(
        ticketId = this.ticketId ?: 0,
        fecha = this.fecha.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")),
        descripcion = this.descripcion ?: "",
        asunto = this.asunto ?: "",
        cliente = this.cliente ?: "",
        prioridadId = this.prioridadId ?: 0
    )
}
