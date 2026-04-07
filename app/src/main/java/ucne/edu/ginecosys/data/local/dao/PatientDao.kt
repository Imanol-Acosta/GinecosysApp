package ucne.edu.ginecosys.data.local.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ucne.edu.ginecosys.data.local.entities.PatientEntity
import ucne.edu.ginecosys.data.local.entities.SyncStatus

@Dao
interface PatientDao {
    @Query("SELECT * FROM patients WHERE syncStatus != 'PENDING_DELETE' ORDER BY firstName ASC")
    fun observeAllPatients(): Flow<List<PatientEntity>>

    @Query("SELECT * FROM patients WHERE id = :id AND syncStatus != 'PENDING_DELETE'")
    suspend fun getPatientById(id: String): PatientEntity?

    @Query("SELECT * FROM patients WHERE syncStatus IN ('PENDING_INSERT', 'PENDING_UPDATE', 'PENDING_DELETE')")
    suspend fun getUnsyncedPatients(): List<PatientEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPatient(patient: PatientEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPatients(patients: List<PatientEntity>)

    @Update
    suspend fun updatePatient(patient: PatientEntity)

    @Query("UPDATE patients SET syncStatus = :status WHERE id = :id")
    suspend fun updateSyncStatus(id: String, status: SyncStatus)

    @Delete
    suspend fun deletePatient(patient: PatientEntity)
}
