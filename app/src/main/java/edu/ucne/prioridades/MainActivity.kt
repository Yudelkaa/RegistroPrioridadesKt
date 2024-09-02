package edu.ucne.prioridades

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.room.Room
import edu.ucne.prioridades.local.database.PrioridadDb
import edu.ucne.prioridades.local.entities.PrioridadEntity
import edu.ucne.prioridades.ui.theme.PrioridadesTheme
import kotlinx.coroutines.launch
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment
import edu.ucne.prioridades.local.dao.PrioridadDao

class MainActivity : ComponentActivity() {
    private lateinit var prioridadDb: PrioridadDb

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        prioridadDb = Room.databaseBuilder(
            applicationContext,
            PrioridadDb::class.java,
            "Prioridad.db"
        ).fallbackToDestructiveMigration().build()
        setContent {
            PrioridadesTheme {
                Principal(prioridadDb)
            }
        }
    }

    @Composable
    fun Principal(prioridadDb: PrioridadDb) {
        var mostrarScreen by remember { mutableStateOf(false) }

        Scaffold(
            floatingActionButton = {
                if (!mostrarScreen) {
                    FloatingActionButton(
                        onClick = { mostrarScreen = true },
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = "Añadir Prioridad")
                    }
                }
            }
        ) {
            if (mostrarScreen) {
                PrioridadScreen(onNavigateToMyScreen = { mostrarScreen = false }, prioridadDb)
            } else {
                PrioridadListScreen(
                    prioridadDb.prioridadDao().getAll()
                        .collectAsStateWithLifecycle(
                            initialValue = emptyList(),
                            lifecycleOwner = LocalLifecycleOwner.current,
                            minActiveState = Lifecycle.State.STARTED
                        ).value,
                    Modifier.padding(it)
                )
            }
        }
    }

    @Composable
    fun PrioridadScreen(onNavigateToMyScreen: () -> Unit, prioridadDb: PrioridadDb) {
        var descripcion by remember { mutableStateOf("") }
        var diasCompromiso by remember { mutableStateOf("") }
        val scope = rememberCoroutineScope()
        var validacion by remember { mutableStateOf<String?>(null) }

        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            floatingActionButton = {
                FloatingActionButton(onClick = onNavigateToMyScreen) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                }
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .padding(8.dp)
            ) {
                Text(
                    text = "Registro de prioridades :D",
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = "Descripción") },
                    value = descripcion,
                    onValueChange = { descripcion = it }
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = "Días compromiso") },
                    value = diasCompromiso,
                    onValueChange = { diasCompromiso = it }
                )
                Spacer(modifier = Modifier.height(16.dp))

                if (validacion != null) {
                    Text(
                        text = validacion!!,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                OutlinedButton(
                    modifier = Modifier.width(200.dp).align(Alignment.CenterHorizontally),
                    onClick = {
                        scope.launch {
                            try {
                                val nuevaPrioridad = PrioridadEntity(
                                    descripcion = descripcion,
                                    diasCompromiso = diasCompromiso.toIntOrNull() ?: 0
                                )
                                guardarPrioridad(prioridadDb.prioridadDao(), nuevaPrioridad)
                                descripcion = ""
                                diasCompromiso = ""
                                validacion = null
                                onNavigateToMyScreen()
                            } catch (e: IllegalArgumentException) {
                                validacion = e.message
                            }
                        }
                    }
                ) {
                    Text(text = "Guardar")
                    Icon(Icons.Default.Add, contentDescription = "Guardar")
                }
            }
        }
    }

    @Composable
    fun PrioridadListScreen(prioridadList: List<PrioridadEntity>, modifier: Modifier = Modifier) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "ID",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Descripción",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(3f),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Días Compromiso",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(prioridadList) { prioridad ->
                    PrioridadRow(prioridad)
                }
            }
        }
    }

    @Composable
    fun PrioridadRow(prioridad: PrioridadEntity) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
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
        HorizontalDivider()
    }
}

suspend fun guardarPrioridad(dao: PrioridadDao, prioridad: PrioridadEntity) {
    if (prioridad.descripcion.isBlank()) {
        throw IllegalArgumentException("Favor ingresar la descripción")
    }
    if (prioridad.diasCompromiso == null || prioridad.diasCompromiso == 0) {
        throw IllegalArgumentException("Favor ingresar un número mayor a cero")
    }
    val existePrioridad = dao.findByDescripcion(prioridad.descripcion)
    if (existePrioridad != null && existePrioridad.prioridadId != prioridad.prioridadId) {
        throw IllegalArgumentException("Ya existe una prioridad con esta descripción.")
    }
    dao.save(prioridad)
}


