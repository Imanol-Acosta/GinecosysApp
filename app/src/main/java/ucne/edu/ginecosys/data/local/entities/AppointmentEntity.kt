package ucne.edu.ginecosys.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "appointments")
data class AppointmentEntity(
    @PrimaryKey
    val id: String,
    val patientId: String,
    val patientName: String,
    val startTime: String,
    val endTime: String,
    val type: String,
    val status: String,
    val notes: String?,
    
    // Offline sync
    val syncStatus: SyncStatus,
    val updatedAt: Long = System.currentTimeMillis()
)
