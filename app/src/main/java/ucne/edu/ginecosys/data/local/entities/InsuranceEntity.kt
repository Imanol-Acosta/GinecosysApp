package ucne.edu.ginecosys.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import ucne.edu.ginecosys.domain.model.Insurance

@Entity(tableName = "insurances")
data class InsuranceEntity(
    @PrimaryKey val id: String,
    val name: String,
    val type: String,
    val phone: String?,
    val isActive: Boolean = true,
    val syncStatus: SyncStatus = SyncStatus.PENDING_INSERT,
    val updatedAt: Long = System.currentTimeMillis()
)

fun InsuranceEntity.toDomain(): Insurance = Insurance(
    id = id,
    name = name,
    type = type,
    phone = phone ?: "",
    isActive = isActive
)

fun Insurance.toEntity(syncStatus: SyncStatus = SyncStatus.PENDING_INSERT): InsuranceEntity =
    InsuranceEntity(
        id = id,
        name = name,
        type = type,
        phone = phone,
        isActive = isActive,
        syncStatus = syncStatus
    )
