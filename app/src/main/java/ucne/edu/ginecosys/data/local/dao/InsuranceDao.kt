package ucne.edu.ginecosys.data.local.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ucne.edu.ginecosys.data.local.entities.InsuranceEntity
import ucne.edu.ginecosys.data.local.entities.SyncStatus

@Dao
interface InsuranceDao {
    @Query("SELECT * FROM insurances WHERE syncStatus != 'PENDING_DELETE' ORDER BY name ASC")
    fun observeAll(): Flow<List<InsuranceEntity>>

    @Query("SELECT * FROM insurances WHERE id = :id")
    suspend fun getById(id: String): InsuranceEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: InsuranceEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entities: List<InsuranceEntity>)

    @Update
    suspend fun update(entity: InsuranceEntity)

    @Query("UPDATE insurances SET syncStatus = :status WHERE id = :id")
    suspend fun updateSyncStatus(id: String, status: SyncStatus)

    @Query("SELECT * FROM insurances WHERE syncStatus IN ('PENDING_INSERT', 'PENDING_UPDATE', 'PENDING_DELETE')")
    suspend fun getUnsynced(): List<InsuranceEntity>

    @Delete
    suspend fun delete(entity: InsuranceEntity)
}
