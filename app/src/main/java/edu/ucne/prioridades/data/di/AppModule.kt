package edu.ucne.prioridades.data.di

import android.content.Context
import androidx.room.Room
import com.google.android.datatransport.runtime.dagger.Module
import com.google.android.datatransport.runtime.dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import edu.ucne.prioridades.data.local.dao.PrioridadDao
import edu.ucne.prioridades.data.local.database.PrioridadDb
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    @Singleton
    fun providePrioridadDb(@ApplicationContext appContext: Context): PrioridadDb =
        Room.databaseBuilder(
            appContext,
            PrioridadDb::class.java,
            "Prioridad.db"
        ).fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun providePrioridadDao(prioridadDb: PrioridadDb): PrioridadDao =
        prioridadDb.prioridadDao()
}
