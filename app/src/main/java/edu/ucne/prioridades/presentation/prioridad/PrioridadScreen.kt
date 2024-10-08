package edu.ucne.prioridades.presentation.prioridad

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch


@Composable
fun PrioridadScreen(
    viewModel: PrioridadViewModel = hiltViewModel(),
    inicialPrioridadId: Int,
    goBack: () -> Unit,
    onNavigateToTickets: () -> Unit,
    onNavigateToPrioridades: () -> Unit,
    openDrawer: () -> Unit,
    prioridadId: Int,
    onNavigateToSistemas: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            edu.ucne.prioridades.presentation.TopAppBar(
                title = "Registro prioridad",
                onMenuClick = openDrawer
            )
        },
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            PrioridadBodyScreen(
                uiState = uiState,
                onEvent = viewModel::onEvent,
                goBack = goBack,
                savePrioridad = { viewModel.save() },
                inicialPrioridadId = inicialPrioridadId,
                viewModel = viewModel,
                onNavigateToTickets = onNavigateToTickets,
                onNavigateToPrioridades = onNavigateToPrioridades,
                openDrawer = openDrawer,
                drawerState = { }
            )
        }
    }
}

@Composable
fun PrioridadBodyScreen(
    uiState: UiState,
    onEvent: (PrioridadUiState) -> Unit,
    goBack: () -> Unit,
    savePrioridad: () -> Unit,
    inicialPrioridadId: Int,
    viewModel: PrioridadViewModel,
    onNavigateToTickets: () -> Unit,
    onNavigateToPrioridades: () -> Unit,
    openDrawer: () -> Unit,
    drawerState: () -> Unit
) {
    val scope = rememberCoroutineScope()
    var descripcion by remember { mutableStateOf(uiState.descripcion ?: "") }
    var diasCompromiso by remember { mutableStateOf(uiState.diasCompromiso?.toString() ?: "") }
    var errorMessage by remember { mutableStateOf(uiState.errorMessage) }

    LaunchedEffect(inicialPrioridadId) {
        if (inicialPrioridadId > 0) {
            onEvent(PrioridadUiState.SelectedPrioridad(inicialPrioridadId))
        }
    }

    LaunchedEffect(uiState) {
        descripcion = uiState.descripcion ?: ""
        diasCompromiso = uiState.diasCompromiso?.toString() ?: ""
        errorMessage = uiState.errorMessage
    }

    val diasCompromisoInt = diasCompromiso.toIntOrNull()
    val isDescripcionValid = descripcion.isNotBlank()
    val isDiasCompromisoValid = diasCompromisoInt != null && diasCompromisoInt > 0
    val isValid = isDescripcionValid && isDiasCompromisoValid

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = descripcion,
            onValueChange = {
                descripcion = it
                errorMessage = null
                onEvent(PrioridadUiState.DescriptionChange(it))
            },
            label = { Text("Descripción") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = diasCompromiso,
            onValueChange = {
                diasCompromiso = it
                val dias = it.toIntOrNull()
                if (dias != null) {
                    onEvent(PrioridadUiState.DaysChange(dias))
                    errorMessage = null
                } else {
                    errorMessage = "Días inválidos"
                }
            },
            label = { Text("Días de Compromiso") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (errorMessage != null) {
            Text(
                text = errorMessage ?: "",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(vertical = 16.dp)
            )
        }

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = {
                    if (isValid) {
                        savePrioridad()
                        goBack()
                    }
                },
                enabled = isValid
            ) {
                Text(text = "Guardar")
                Icon(Icons.Default.Add, contentDescription = "Add")
            }

            if (inicialPrioridadId != 0) {
                Button(
                    onClick = {
                        scope.launch {
                            try {
                                viewModel.onEvent(PrioridadUiState.Delete)
                                goBack()
                            } catch (e: Exception) {
                                errorMessage = "Error al eliminar la prioridad."
                            }
                        }
                    }
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Eliminar")
                }
            }
        }
    }
}
