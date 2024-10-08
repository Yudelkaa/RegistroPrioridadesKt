package edu.ucne.prioridades.data.repository

import android.util.Log
import edu.ucne.prioridades.data.remote.SistemasApi
import edu.ucne.prioridades.data.remote.dto.SistemaDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SistemaRepository @Inject constructor(
    private val sistemaApi: SistemasApi
){
    fun getSistemas(): Flow<Resource<List<SistemaDto>>> = flow{
        emit(Resource.Loading())
        try{
            val sistemas = sistemaApi.getSistemas()
            emit(Resource.Success(sistemas))
        }catch(e: Exception){
            Log.e("SistemasRepository", "getSistemas: ${e.message}")
            emit(Resource.Error(e.message ?: "Error"))
        }
    }
    suspend fun getSistema(id: Int): SistemaDto?{
        return try{
            sistemaApi.getSistema(id)
        }catch(e: Exception){
            Log.e("SistemasRepository", "getSistema: ${e.message}")
            val sistema: SistemaDto? = null
            sistema
        }
    }

    suspend fun addSistemas(sistema: SistemaDto){
        sistemaApi.addSistema(sistema)
    }

    suspend fun updateSistemas(sistema: SistemaDto){
        sistemaApi.updateSistema(sistema.id, sistema)
    }

    suspend fun deleteSistemas(id: Int){
        sistemaApi.deleteSistema(id)
    }
}

sealed class Resource<T>(val data: T? = null, val message: String? = null){
    class Loading<T>(data: T? = null) : Resource<T>(data)
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String, data : T? = null) : Resource<T>(data, message)
}



