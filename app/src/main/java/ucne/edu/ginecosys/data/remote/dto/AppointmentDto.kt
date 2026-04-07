package ucne.edu.ginecosys.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ucne.edu.ginecosys.data.local.entities.AppointmentEntity
import ucne.edu.ginecosys.data.local.entities.SyncStatus

@JsonClass(generateAdapter = true)
data class AppointmentDto(
    val id: String,
    @Json(name = "clinic_id") val clinicId: String,
    @Json(name = "patient_id") val patientId: String,
    @Json(name = "patient_name") val patientName: String,
    @Json(name = "start_time") val startTime: String,
    @Json(name = "end_time") val endTime: String,
    val type: String,
    val status: String,
    val notes: String?
)

fun AppointmentDto.toEntity(): AppointmentEntity {
    return AppointmentEntity(
        id = id,
        patientId = patientId,
        patientName = patientName,
        startTime = startTime,
        endTime = endTime,
        type = type,
        status = status,
        notes = notes,
        syncStatus = SyncStatus.SYNCED
    )
}

fun AppointmentEntity.toDto(clinicId: String): AppointmentDto {
    return AppointmentDto(
        id = id,
        clinicId = clinicId,
        patientId = patientId,
        patientName = patientName,
        startTime = startTime,
        endTime = endTime,
        type = type,
        status = status,
        notes = notes
    )
}
