package edu.ucne.prioridades.presentation.ticket

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import androidx.compose.ui.Alignment


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketScreen(
    viewModel: TicketViewModel = hiltViewModel(),
    ticketId: Int,
    goBack: () -> Unit,
    openDrawer: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(ticketId) {
        viewModel.setTicketId(ticketId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registro de Tickets") },
                navigationIcon = {
                    IconButton(onClick = goBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = openDrawer) {
                        Icon(Icons.Filled.Menu, contentDescription = "Open Drawer")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            TicketBodyScreen(
                uiState = uiState,
                goBack = goBack,
                saveTicket = { viewModel.save() },
                deleteTicket = { viewModel.deleteTicket(ticketId) },
                ticketId = ticketId
            )
        }
    }
}

@Composable
fun TicketBodyScreen(
    uiState: TicketUiState,
    goBack: () -> Unit,
    saveTicket: () -> Unit,
    deleteTicket: () -> Unit,
    ticketId: Int,
    viewModel: TicketViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    var showDatePicker by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = uiState.descripcion ?: "",
            onValueChange = { newValue -> viewModel.onDescripcionChange(newValue) },
            label = { Text("Descripción") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            isError = uiState.descripcionError != null
        )
        if (uiState.descripcionError != null) {
            Text(
                text = uiState.descripcionError ?: "",
                color = Color.Red,
                fontStyle = FontStyle.Italic,
                fontSize = 14.sp
            )
        }

        OutlinedTextField(
            value = uiState.asunto ?: "",
            onValueChange = { newValue -> viewModel.onAsuntoChange(newValue) },
            label = { Text("Asunto") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            isError = uiState.asuntoError != null
        )
        if (uiState.asuntoError != null) {
            Text(
                text = uiState.asuntoError ?: "",
                color = Color.Red,
                fontStyle = FontStyle.Italic,
                fontSize = 14.sp
            )
        }

        OutlinedTextField(
            value = uiState.cliente ?: "",
            onValueChange = { newValue -> viewModel.onClienteChange(newValue) },
            label = { Text("Cliente") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            isError = uiState.clienteError != null
        )
        if (uiState.clienteError != null) {
            Text(
                text = uiState.clienteError ?: "",
                color = Color.Red,
                fontStyle = FontStyle.Italic,
                fontSize = 14.sp
            )
        }

        OutlinedTextField(
            value = uiState.fecha?.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")) ?: "",
            readOnly = true,
            onValueChange = {},
            trailingIcon = {
                IconButton(onClick = { showDatePicker = true }) {
                    Icon(Icons.Filled.DateRange, contentDescription = "Date Picker")
                }
            },
            label = { Text("Fecha") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        if (showDatePicker) {
            val currentDate = uiState.fecha ?: LocalDate.now()
            DatePickerDialog(
                context,
                { _, year, month, dayOfMonth ->
                    val selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
                    viewModel.onFechaChange(selectedDate)
                    showDatePicker = false
                },
                currentDate.year,
                currentDate.monthValue - 1,
                currentDate.dayOfMonth
            ).show()
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    if (viewModel.validateTicket()) {
                        saveTicket()
                        goBack()
                    }
                }
            ) {
                Icon(Icons.Filled.Done, contentDescription = "Save Ticket")
                Text(text = "Guardar")
            }

            if (ticketId > 0) {
                Button(
                    onClick = {
                        deleteTicket()
                        goBack()
                    },
                    colors = ButtonDefaults.buttonColors(Color.Red)
                ) {
                    Icon(Icons.Filled.Delete, contentDescription = "Delete Ticket")
                    Text(text = "Borrar")
                }
            }
        }
    }
}
