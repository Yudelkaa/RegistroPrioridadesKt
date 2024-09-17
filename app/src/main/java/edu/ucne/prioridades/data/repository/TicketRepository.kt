package edu.ucne.prioridades.data.repository


import edu.ucne.prioridades.data.local.dao.TicketDao
import edu.ucne.prioridades.data.local.entities.TicketEntity
import javax.inject.Inject

class TicketRepository @Inject constructor(
    private val ticketDao: TicketDao
) {
    suspend fun save(ticket: TicketEntity) = ticketDao.save(ticket)
    suspend fun getTicket(id: Int): TicketEntity? = ticketDao.find(id)
    suspend fun deleteTicket(ticket: TicketEntity) = ticketDao.delete(ticket)
    fun getTickets() = ticketDao.getAll()
    suspend fun findByDescripcion(descripcion: String): Boolean {
        return ticketDao.findByDescripcion(descripcion) != null
    }


}
