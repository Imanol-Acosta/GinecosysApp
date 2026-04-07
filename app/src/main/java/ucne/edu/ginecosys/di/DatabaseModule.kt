package ucne.edu.ginecosys.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ucne.edu.ginecosys.data.local.AppDatabase
import ucne.edu.ginecosys.data.local.dao.PatientDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "ginecosys_db"
        ).fallbackToDestructiveMigration()
        .build()
    }

    @Provides
    fun providePatientDao(database: AppDatabase): ucne.edu.ginecosys.data.local.dao.PatientDao {
        return database.patientDao()
    }

    @Provides
    fun provideAppointmentDao(database: AppDatabase): ucne.edu.ginecosys.data.local.dao.AppointmentDao {
        return database.appointmentDao()
    }
}
