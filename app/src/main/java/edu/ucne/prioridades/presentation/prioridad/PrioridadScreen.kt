import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.prioridades.presentation.prioridad.PrioridadUiState
import edu.ucne.prioridades.presentation.prioridad.PrioridadViewModel
import edu.ucne.prioridades.presentation.prioridad.UiState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrioridadScreen(
    viewModel: PrioridadViewModel = hiltViewModel(),
    inicialPrioridadId: Int,
    goBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    PrioridadBodyScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        goBack = goBack,
        savePrioridad = { viewModel.save() },
        inicialPrioridadId = inicialPrioridadId,
        viewModel = viewModel
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrioridadBodyScreen(
    uiState: UiState,
    onEvent: (PrioridadUiState) -> Unit,
    goBack: () -> Unit,
    savePrioridad: () -> Unit,
    inicialPrioridadId: Int,
    viewModel: PrioridadViewModel
) {
    val scope = rememberCoroutineScope()

    // Mantén la referencia actual del estado UI
    val uiStateRef = rememberUpdatedState(uiState)

    var descripcion by remember { mutableStateOf(uiState.descripcion ?: "") }
    var diasCompromiso by remember { mutableStateOf(uiState.diasCompromiso?.toString() ?: "") }
    var errorMessage by remember { mutableStateOf(uiState.errorMessage) }

    LaunchedEffect(inicialPrioridadId) {
        if (inicialPrioridadId > 0) {
            onEvent(PrioridadUiState.SelectedPrioridad(inicialPrioridadId))
        }
    }

    LaunchedEffect(uiStateRef.value) {
        descripcion = uiStateRef.value.descripcion ?: ""
        diasCompromiso = uiStateRef.value.diasCompromiso?.toString() ?: ""
        errorMessage = uiStateRef.value.errorMessage
    }

    // Validaciones
    val diasCompromisoInt = diasCompromiso.toIntOrNull()
    val isDescripcionValid = descripcion.isNotBlank()
    val isDiasCompromisoValid = diasCompromisoInt != null && diasCompromisoInt > 0
    val isValid = isDescripcionValid && isDiasCompromisoValid

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = if (inicialPrioridadId == 0) "Nueva Prioridad" else "Editar Prioridad") },
                navigationIcon = {
                    IconButton(onClick = goBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                OutlinedTextField(
                    value = descripcion,
                    onValueChange = {
                        descripcion = it
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
    )
}
