package edu.ucne.prioridades.presentation.ticket

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import edu.ucne.prioridades.data.local.entities.TicketEntity
import edu.ucne.prioridades.data.repository.PrioridadRepository
import edu.ucne.prioridades.data.repository.TicketRepository
import java.time.format.DateTimeFormatter
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketListScreen(
    viewModel: TicketViewModel = hiltViewModel(),
    goToTicket: (Int) -> Unit,
    openDrawer: () -> Unit,
    onNavigateToPrioridades: () -> Unit,
    onNavigateToTickets: () -> Unit,
    prioridadRepository: PrioridadRepository,
    ticketRepository: TicketRepository,
    navHost: NavHostController
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

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
            FloatingActionButton(
                onClick = { goToTicket(0) },
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Añadir Ticket")
            }
        },
        content = { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp)
            ) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Text(
                            modifier = Modifier.weight(3f),
                            text = "Descripción",
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Center
                        )

                        Text(
                            modifier = Modifier.weight(1f),
                            text = "ID",
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Center
                        )

                        Text(
                            modifier = Modifier.weight(2f),
                            text = "Asunto",
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            modifier = Modifier.weight(2f),
                            text = "Cliente",
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            modifier = Modifier.weight(1f),
                            text = "PrioridadId",
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            modifier = Modifier.weight(2f),
                            text = "Fecha",
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                items(uiState.tickets) { ticket ->
                    TicketRow(ticket, onDelete = {})
                }
            }
        }
    )
}
@Composable
fun TicketRow(
    ticket: TicketEntity,
    onDelete: (Int) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
            .clickable { onDelete(ticket.ticketId!!) } // Aquí se elimina directamente al hacer clic
    ) {
        Text(
            modifier = Modifier.weight(3f),
            text = ticket.descripcion ?: "",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        Text(
            modifier = Modifier.weight(1f),
            text = ticket.ticketId.toString(),
            textAlign = TextAlign.Center
        )
        Text(
            modifier = Modifier.weight(2f),
            text = ticket.asunto ?: "",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        Text(
            modifier = Modifier.weight(2f),
            text = ticket.cliente ?: "",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        Text(
            modifier = Modifier.weight(1f),
            text = ticket.prioridadId.toString(),
            textAlign = TextAlign.Center
        )
        Text(
            modifier = Modifier.weight(2f),
            text = ticket.fecha?.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")) ?: "",
            textAlign = TextAlign.Center
        )
    }
}

