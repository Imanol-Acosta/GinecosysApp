package ucne.edu.ginecosys.data.local.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ucne.edu.ginecosys.data.local.entities.ConsultationEntity
import ucne.edu.ginecosys.data.local.entities.SyncStatus

@Dao
interface ConsultationDao {
    @Query("SELECT * FROM consultations WHERE patientId = :patientId AND syncStatus != 'PENDING_DELETE' ORDER BY date DESC")
    fun observeByPatient(patientId: String): Flow<List<ConsultationEntity>>

    @Query("SELECT * FROM consultations WHERE syncStatus != 'PENDING_DELETE' ORDER BY date DESC")
    fun observeAll(): Flow<List<ConsultationEntity>>

    @Query("SELECT * FROM consultations WHERE id = :id AND syncStatus != 'PENDING_DELETE'")
    suspend fun getById(id: String): ConsultationEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(consultation: ConsultationEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(consultations: List<ConsultationEntity>)

    @Update
    suspend fun update(consultation: ConsultationEntity)

    @Query("UPDATE consultations SET syncStatus = :status WHERE id = :id")
    suspend fun updateSyncStatus(id: String, status: SyncStatus)

    @Query("SELECT id FROM consultations WHERE syncStatus IN ('PENDING_INSERT', 'PENDING_UPDATE', 'PENDING_DELETE')")
    suspend fun getUnsyncedIds(): List<String>

    @Query("SELECT * FROM consultations WHERE syncStatus IN ('PENDING_INSERT', 'PENDING_UPDATE', 'PENDING_DELETE')")

    suspend fun getUnsynced(): List<ConsultationEntity>

    @Delete
    suspend fun delete(consultation: ConsultationEntity)
}
