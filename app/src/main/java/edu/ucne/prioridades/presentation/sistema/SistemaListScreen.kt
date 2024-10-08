package edu.ucne.prioridades.presentation.sistema

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.prioridades.data.remote.dto.SistemaDto
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

@Composable
fun SistemaListScreen(
    viewModel: SistemaViewModel = hiltViewModel(),
    onAdd: () -> Unit,
    onEdit: (Int) -> Unit,
    onVerSistema: (SistemaDto) -> Unit,
    openDrawer: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    SistemaListBody(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onAdd = onAdd,
        onVerSistema = onVerSistema,
        openDrawer = openDrawer
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SistemaListBody(
    uiState: SistemaUiState,
    onEvent: (SistemaEvent) -> Unit,
    onAdd: () -> Unit,
    onVerSistema: (SistemaDto) -> Unit,
    openDrawer: () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Lista de Sistemas",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { openDrawer() }) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = "Abrir drawer"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAdd) {
                Icon(Icons.Filled.Add, contentDescription = "Agregar")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            HeaderRow()
            Divider()
            SistemaList(
                uiState = uiState,
                onEvent = onEvent,
                onVerSistema = onVerSistema
            )
        }
    }
}

@Composable
fun HeaderRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Id", modifier = Modifier.weight(0.3f))
        Text(text = "Nombre del Sistema", modifier = Modifier.weight(0.7f))
    }
}

@Composable
fun SistemaList(
    uiState: SistemaUiState,
    onEvent: (SistemaEvent) -> Unit,
    onVerSistema: (SistemaDto) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(uiState.sistemas, key = { it.id }) { sistema ->
            SistemaRow(
                sistema = sistema,
                onEvent = onEvent,
                onVerSistema = onVerSistema
            )
            Divider(modifier = Modifier.padding(horizontal = 20.dp))
        }
    }
}

@Composable
fun SistemaRow(
    sistema: SistemaDto,
    onEvent: (SistemaEvent) -> Unit,
    onVerSistema: (SistemaDto) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { state ->
            if (state == SwipeToDismissBoxValue.EndToStart) {
                coroutineScope.launch {
                    delay(0.5.seconds)
                    onEvent(SistemaEvent.Borrar(sistema.id))
                }
                true
            } else {
                false
            }
        }
    )

    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromStartToEnd = false,
        backgroundContent = {
            val color by animateColorAsState(
                when (dismissState.targetValue) {
                    SwipeToDismissBoxValue.Settled -> Color.White
                    SwipeToDismissBoxValue.EndToStart -> Color.Red
                    else -> Color.Transparent
                }
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color, shape = RoundedCornerShape(8.dp))
                    .padding(16.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar",
                    tint = Color.White
                )
            }
        },
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onVerSistema(sistema) }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            sistema.id?.let { id ->
                Text(
                    text = id.toString(),
                    modifier = Modifier.weight(0.3f),
                    fontWeight = FontWeight.Bold
                )
            }

            sistema.nombre?.let { nombre ->
                Text(
                    text = nombre,
                    modifier = Modifier.weight(0.7f)
                )
            }
        }
    }
}
