package edu.ucne.prioridades.data.repository

import edu.ucne.prioridades.data.local.dao.PrioridadDao
import edu.ucne.prioridades.data.local.entities.PrioridadEntity
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


class PrioridadRepository @Inject constructor(
    private val prioridadDao: PrioridadDao
) {
    suspend fun save(prioridad: PrioridadEntity) = prioridadDao.save(prioridad)
    suspend fun getPrioridad(id: Int) = prioridadDao.find(id)
    suspend fun delete(prioridad: PrioridadEntity) = prioridadDao.delete(prioridad)
    fun getPrioridades() = prioridadDao.getAll()
    suspend fun findByDescripcion(descripcion: String): Boolean {
        return prioridadDao.findByDescripcion(descripcion) != null
    }

}