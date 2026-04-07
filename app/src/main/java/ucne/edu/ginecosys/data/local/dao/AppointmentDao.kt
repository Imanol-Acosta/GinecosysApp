package ucne.edu.ginecosys.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ucne.edu.ginecosys.data.local.entities.AppointmentEntity
import ucne.edu.ginecosys.data.local.entities.SyncStatus

@Dao
interface AppointmentDao {
    @Query("SELECT * FROM appointments ORDER BY startTime ASC")
    fun getAppointments(): Flow<List<AppointmentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAppointment(appointment: AppointmentEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAppointments(appointments: List<AppointmentEntity>)

    @Query("SELECT * FROM appointments WHERE id = :id")
    suspend fun getAppointmentById(id: String): AppointmentEntity?

    @Query("SELECT * FROM appointments WHERE syncStatus = :status")
    suspend fun getAppointmentsBySyncStatus(status: SyncStatus): List<AppointmentEntity>

    @Query("UPDATE appointments SET syncStatus = :newStatus WHERE id = :id")
    suspend fun updateSyncStatus(id: String, newStatus: SyncStatus)
}
