package edu.ucne.prioridades.presentation.ticket

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.prioridades.data.local.entities.TicketEntity
import edu.ucne.prioridades.data.local.entities.PrioridadEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketListScreen(
    viewModel: TicketViewModel = hiltViewModel(),
    goToTicket: (Int) -> Unit,
    openDrawer: () -> Unit,
) {

    val tickets by viewModel.ticket.collectAsStateWithLifecycle()
    val prioridad by viewModel.prioridad.collectAsStateWithLifecycle()

    TicketListBody(
        tickets = tickets,
        prioridad = prioridad,
        openDrawer = openDrawer,
        goToTicket = goToTicket,
        onDeleteTicket = { ticketId ->
            if (ticketId > 0) {
                viewModel.deleteTicket(ticketId)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketListBody(
    tickets: List<TicketEntity>,
    prioridad: List<PrioridadEntity>,
    openDrawer: () -> Unit,
    goToTicket: (Int) -> Unit,
    onDeleteTicket: (Int) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lista de Tickets") },
                navigationIcon = {
                    IconButton(onClick = openDrawer) {
                        Icon(Icons.Default.Menu, contentDescription = "Abrir Menú")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { goToTicket(-1) }) {
                Icon(Icons.Default.Add, contentDescription = "Añadir Ticket")
            }
        },
        content = { paddingValues ->
            Column(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Descripción", modifier = Modifier.weight(0.35f))
                    Text("Cliente", modifier = Modifier.weight(0.30f))
                    Text("Prioridad", modifier = Modifier.weight(0.25f))
                    Text("Asunto", modifier = Modifier.weight(0.25f))
                    Text("Fecha", modifier = Modifier.weight(0.25f))
                }
                Divider()

                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(tickets) { ticket ->
                        TicketRow(
                            ticket = ticket,
                            onClick = { ticket.ticketId?.let { goToTicket(it) } },
                            onDelete = { ticket.ticketId?.let { onDeleteTicket(it) } }
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun TicketRow(
    ticket: TicketEntity,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = ticket.descripcion ?: "", modifier = Modifier.weight(0.35f))
        Text(text = ticket.cliente ?: "", modifier = Modifier.weight(0.30f))
        Text(text = ticket.prioridadId.toString(), modifier = Modifier.weight(0.25f))
        Text(text = ticket.asunto ?: "", modifier = Modifier.weight(0.25f))
        Text(text = ticket.fecha.toString(), modifier = Modifier.weight(0.25f))
        Spacer(modifier = Modifier.weight(0.1f))
        IconButton(onClick = { onDelete() }) {
            Icon(Icons.Default.Delete, contentDescription = "Eliminar Ticket")
        }
    }
}
