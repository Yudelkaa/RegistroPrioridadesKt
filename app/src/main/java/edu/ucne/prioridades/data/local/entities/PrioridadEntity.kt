package edu.ucne.prioridades.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Prioridades")
data class PrioridadEntity(
    @PrimaryKey (autoGenerate = true)
    val prioridadId: Int? = null,
    var descripcion: String = "",
    var diasCompromiso: Int? = 0
)