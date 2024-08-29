package edu.ucne.prioridades.local.dao

import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import edu.ucne.prioridades.local.database.PrioridadDb
import edu.ucne.prioridades.local.entities.PrioridadEntity
import kotlinx.coroutines.flow.Flow

interface PrioridadDao {
    @Upsert()
    suspend fun save(prioridad: PrioridadEntity)

    @Query(
        """
            SELECT * FROM Prioridades WHERE prioridadId=:id LIMIT 1

        """

    )
    suspend fun find(id: Int): PrioridadEntity?

    @Delete
    suspend fun delete(prioridad: PrioridadEntity)

    @Query("SELECT * FROM Prioridades")
    fun getAll(): Flow<List<PrioridadEntity>>



}