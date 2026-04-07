package ucne.edu.ginecosys.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ucne.edu.ginecosys.data.local.entities.PatientEntity
import ucne.edu.ginecosys.data.local.entities.SyncStatus

@JsonClass(generateAdapter = true)
data class PatientDto(
    val id: String,
    @Json(name = "clinic_id") val clinicId: String,
    @Json(name = "cedula") val cedula: String?,
    @Json(name = "first_name") val firstName: String,
    @Json(name = "last_name") val lastName: String,
    val dob: String?,
    val phone: String?,
    val address: String?,
    @Json(name = "insurance_provider") val insuranceProvider: String?
)

fun PatientDto.toEntity(): PatientEntity {
    return PatientEntity(
        id = id,
        firstName = firstName,
        lastName = lastName,
        cedula = cedula,
        dob = dob,
        phone = phone,
        address = address,
        insuranceProvider = insuranceProvider,
        syncStatus = SyncStatus.SYNCED
    )
}

fun PatientEntity.toDto(clinicId: String): PatientDto {
    return PatientDto(
        id = id,
        clinicId = clinicId,
        cedula = cedula,
        firstName = firstName,
        lastName = lastName,
        dob = dob,
        phone = phone,
        address = address,
        insuranceProvider = insuranceProvider
    )
}
