package edu.ucne.prioridades.presentation.sistema

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SistemaScreen(
    viewModel: SistemaViewModel = hiltViewModel(),
    goSistemaList: () -> Unit,
    id: Int,
    goBack: () -> Boolean,
    openDrawer: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(key1 = id) {
        if (uiState.id == 0) {
            viewModel.onEvent(SistemaEvent.ObtenerSistema(id))
        }
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Sistema") },
                navigationIcon = {
                    IconButton(onClick = { goBack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { openDrawer() }) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = "OpenDrawer"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        SistemaBodyScreen(
            uiState = uiState,
            onEvent = viewModel::onEvent,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun SistemaBodyScreen(
    uiState: SistemaUiState,
    onEvent: (SistemaEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        SistemaContent(
            uiState = uiState,
            onEvent = onEvent
        )
    }
}
@Composable
fun SistemaContent(
    uiState: SistemaUiState,
    onEvent: (SistemaEvent) -> Unit,
) {
    OutlinedTextField(
        value = uiState.nombre ?: "",
        onValueChange = { onEvent(SistemaEvent.NombreChanged(it)) },
        label = { Text(text = "Nombre") },
        modifier = Modifier.fillMaxWidth(),
        isError = uiState.nombreError != null
    )

    if (uiState.nombreError != null) {
        Text(
            text = uiState.nombreError!!,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall
        )
    }

    Spacer(modifier = Modifier.height(16.dp))

    ActionButtons(
        onGuardar = { onEvent(SistemaEvent.Guardar) },
        onNuevo = { onEvent(SistemaEvent.Nuevo) }
    )
}


@Composable
fun ActionButtons(
    onGuardar: () -> Unit,
    onNuevo: () -> Unit
) {
    Row {
        OutlinedButton(onClick = onGuardar) {
            Text(text = "Guardar")
        }

        Spacer(modifier = Modifier.width(16.dp))

        OutlinedButton(onClick = onNuevo) {
            Text(text = "Nuevo")
        }
    }
}
