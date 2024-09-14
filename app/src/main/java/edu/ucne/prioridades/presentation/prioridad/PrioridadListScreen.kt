package edu.ucne.prioridades.presentation.prioridad

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.prioridades.data.local.entities.PrioridadEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrioridadListScreen(
    viewModel: PrioridadViewModel = hiltViewModel(),
    goToPrioridad: (Int) -> Unit,
    goToAddPrioridad: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val prioridades = uiState.prioridades

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lista de Prioridades") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = goToAddPrioridad,
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Añadir Prioridad")
            }
        },
        content = { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(prioridades) { prioridad ->
                    PrioridadRow(
                        prioridad = prioridad,
                        goToPrioridad = { id -> goToPrioridad(id) }
                    )
                }
            }
        }
    )
}

@Composable
fun PrioridadRow(
    prioridad: PrioridadEntity,
    goToPrioridad: (Int) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
            .clickable { goToPrioridad(prioridad.prioridadId ?: 0) }
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = prioridad.prioridadId.toString(),
            textAlign = TextAlign.Center
        )

        Text(
            modifier = Modifier.weight(3f),
            text = prioridad.descripcion,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )

        Text(
            modifier = Modifier.weight(1f),
            text = prioridad.diasCompromiso.toString(),
            textAlign = TextAlign.Center
        )
    }
    Divider()
}
