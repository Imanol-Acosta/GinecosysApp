package ucne.edu.ginecosys.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import ucne.edu.ginecosys.domain.model.Patient

@Entity(tableName = "patients")
data class PatientEntity(
    @PrimaryKey
    val id: String,
    val firstName: String,
    val lastName: String,
    val cedula: String?,
    val dob: String?,
    val phone: String?,
    val address: String?,
    val insuranceProvider: String?,
    
    // Sync Metadata
    val syncStatus: SyncStatus = SyncStatus.PENDING_INSERT,
    val updatedAt: Long = System.currentTimeMillis()
)

fun PatientEntity.toDomain(): Patient {
    return Patient(
        id = id,
        nombre = firstName,
        apellidos = lastName,
        cedula = cedula ?: "",
        fechaNacimiento = dob ?: "",
        ars = insuranceProvider ?: "",
        direccion = address ?: "",
        telefono = phone ?: ""
    )
}

fun Patient.toEntity(syncStatus: SyncStatus = SyncStatus.PENDING_INSERT): PatientEntity {
    return PatientEntity(
        id = id,
        firstName = nombre,
        lastName = apellidos,
        cedula = cedula,
        dob = fechaNacimiento,
        phone = telefono,
        address = direccion,
        insuranceProvider = ars,
        syncStatus = syncStatus
    )
}
