package edu.ucne.prioridades.presentation.prioridad

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import edu.ucne.prioridades.data.local.database.PrioridadDb
import edu.ucne.prioridades.data.local.entities.PrioridadEntity
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrioridadScreen(
    prioridadDb: PrioridadDb,
    goBack: () -> Unit,
    prioridadId: Int
) {
    val dao = prioridadDb.prioridadDao()
    val scope = rememberCoroutineScope()

    var descripcion by remember { mutableStateOf("") }
    var diasCompromiso by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(prioridadId) {
        if (prioridadId != 0) {
            val prioridad = dao.find(prioridadId)
            if (prioridad != null) {
                descripcion = prioridad.descripcion
                diasCompromiso = prioridad.diasCompromiso.toString()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = if (prioridadId == 0) "Nueva Prioridad" else "Editar Prioridad") },
                navigationIcon = {
                    IconButton(onClick = goBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .padding(16.dp)
            ) {
                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = diasCompromiso,
                    onValueChange = { diasCompromiso = it },
                    label = { Text("Días de Compromiso") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = {
                            scope.launch {
                                try {
                                    val prioridad = PrioridadEntity(
                                        prioridadId = if (prioridadId == 0) null else prioridadId,
                                        descripcion = descripcion,
                                        diasCompromiso = diasCompromiso.toIntOrNull() ?: 0
                                    )
                                    dao.save(prioridad)
                                    goBack()
                                } catch (e: Exception) {
                                    errorMessage = e.message
                                }
                            }
                        }
                    ) {
                        Text(text = "Guardar")
                        Icon(Icons.Default.Add, contentDescription = "Add")
                    }

                    if (prioridadId != 0) {
                        Button(
                            onClick = {
                                scope.launch {
                                    try {
                                        dao.delete(
                                            PrioridadEntity(
                                                prioridadId = prioridadId
                                            )
                                        )
                                        goBack()
                                    } catch (e: Exception) {
                                        errorMessage = e.message
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

                if (errorMessage != null) {
                    Text(
                        text = errorMessage ?: ""
                    )
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { goBack() },
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
            }
        }
    )
}
