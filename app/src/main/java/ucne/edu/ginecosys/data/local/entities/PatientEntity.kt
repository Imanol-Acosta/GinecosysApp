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

    // New fields
    val gender: String?,
    val city: String?,
    val bloodType: String?,
    val civilStatus: String?,
    val insuranceId: String?,
    val fum: String?,
    val cycle: String?,
    val menarche: Int?,
    val contraceptiveMethod: String?,
    val sexualActivity: Boolean?,

    // JSON strings for complex objects
    val obstetricHistoryJson: String?,
    val antecedentsJson: String?,
    val activePregnancyJson: String?,
    val pregnanciesJson: String?,

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
        gender = gender ?: "Femenino",
        ars = insuranceProvider ?: "",
        insuranceId = insuranceId ?: "",
        direccion = address ?: "",
        ciudad = city ?: "",
        telefono = phone ?: "",
        bloodType = bloodType ?: "",
        civilStatus = civilStatus ?: "",
        fum = fum ?: "",
        ciclo = cycle ?: "",
        menarquia = menarche,
        planificacion = contraceptiveMethod ?: "",
        sexualActivity = sexualActivity,
        obstetricHistory = ucne.edu.ginecosys.data.local.Converters.obstetricHistoryFromJson(obstetricHistoryJson),
        antecedents = ucne.edu.ginecosys.data.local.Converters.antecedentsFromJson(antecedentsJson),
        activePregnancy = ucne.edu.ginecosys.data.local.Converters.activePregnancyFromJson(activePregnancyJson),
        pregnancies = ucne.edu.ginecosys.data.local.Converters.pregnanciesFromJson(pregnanciesJson)
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
        gender = gender,
        city = ciudad,
        bloodType = bloodType,
        civilStatus = civilStatus,
        insuranceId = insuranceId,
        fum = fum,
        cycle = ciclo,
        menarche = menarquia,
        contraceptiveMethod = planificacion,
        sexualActivity = sexualActivity,
        obstetricHistoryJson = ucne.edu.ginecosys.data.local.Converters.obstetricHistoryToJson(obstetricHistory),
        antecedentsJson = ucne.edu.ginecosys.data.local.Converters.antecedentsToJson(antecedents),
        activePregnancyJson = ucne.edu.ginecosys.data.local.Converters.activePregnancyToJson(activePregnancy),
        pregnanciesJson = ucne.edu.ginecosys.data.local.Converters.pregnanciesToJson(pregnancies),
        syncStatus = syncStatus
    )
}
