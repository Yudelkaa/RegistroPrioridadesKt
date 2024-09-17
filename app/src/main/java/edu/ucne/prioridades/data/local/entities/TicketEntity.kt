package edu.ucne.prioridades.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "Tickets",
    foreignKeys = [
        ForeignKey(
            entity = PrioridadEntity::class,
            parentColumns = ["prioridadId"],
            childColumns = ["prioridadId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class TicketEntity(
    @PrimaryKey(autoGenerate = true)
    val ticketId: Int? = null,
    val cliente: String = "",
    val asunto: String = "",
    var descripcion: String? = "",
    val fecha: String? = "",
    val prioridadId: Int? = null
)


