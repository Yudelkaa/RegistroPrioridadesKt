package edu.ucne.prioridades.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "Tickets")
data class TicketEntity(
    @PrimaryKey(autoGenerate = true)
    val ticketId: Int = 0,
    val cliente: String = "",
    val asunto: String = "",
    var descripcion: String = "",
    val fecha: String? = "",
    val prioridadId: Int? = null
)

