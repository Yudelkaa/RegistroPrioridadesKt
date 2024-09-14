package edu.ucne.prioridades

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import edu.ucne.prioridades.data.local.database.PrioridadDb
import edu.ucne.prioridades.presentation.navegation.PrioridadesAp2NavHost
import edu.ucne.prioridades.ui.theme.PrioridadesTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import dagger.hilt.android.AndroidEntryPoint
import edu.ucne.prioridades.presentation.prioridad.PrioridadListScreen
import edu.ucne.prioridades.data.local.entities.PrioridadEntity

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            PrioridadesTheme {
                val navHost = rememberNavController()
                PrioridadesAp2NavHost(navHost)
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun Preview() {
    val prioridadList = listOf(
        PrioridadEntity(1, "Enel", 9),
        PrioridadEntity(2, "Juan", 9),
    )

}









