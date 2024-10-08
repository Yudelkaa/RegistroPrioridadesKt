package edu.ucne.prioridades.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import edu.ucne.prioridades.data.remote.dto.SistemaDto

interface SistemasApi {
    @GET("api/Sistemas/{id}")
    suspend fun getSistema(@Path("id") id: Int) : SistemaDto

    @GET("api/Sistemas")
    suspend fun getSistemas(): List<SistemaDto>

    @POST("api/Sistemas")
    suspend fun addSistema(@Body sistemaDto: SistemaDto?): SistemaDto

    @PUT("api/Sistemas/{id}")
    suspend fun updateSistema(@Path("id") id: Int, @Body sistemaDto: SistemaDto?): SistemaDto

    @DELETE("api/Sistemas/{id}")
    suspend fun deleteSistema(@Path("id") id: Int): Response<Unit>
}