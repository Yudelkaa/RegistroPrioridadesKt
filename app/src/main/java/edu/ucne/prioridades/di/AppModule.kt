package edu.ucne.prioridades.di

import android.content.Context
import androidx.room.Room
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import edu.ucne.prioridades.data.local.database.PrioridadDb
import edu.ucne.prioridades.data.remote.SistemasApi
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {
    @Provides
    @Singleton
    fun providePrioridadDb(@ApplicationContext appContext: Context) =
        Room.databaseBuilder(
            appContext,
            PrioridadDb::class.java,
            "Prioridad.db"
        ).fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun providePrioridadDao(prioridadDb: PrioridadDb) =
        prioridadDb.prioridadDao()

    @Provides
    @Singleton
    fun provideTicketDao(prioridadDb: PrioridadDb) =
        prioridadDb.ticketDao()

    const val BASE_URL = "https://www.themealdb.com/api/json/v1/1"

    @Singleton
    @Provides
    fun providesMoshi(): Moshi =
        Moshi.Builder().add(KotlinJsonAdapterFactory())
            .build()

    @Provides
    @Singleton
    fun providesSistemasApi(moshi: Moshi): SistemasApi
    {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(SistemasApi::class.java)
    }
}

