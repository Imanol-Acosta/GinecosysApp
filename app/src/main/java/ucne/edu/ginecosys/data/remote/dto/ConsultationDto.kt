package ucne.edu.ginecosys.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ucne.edu.ginecosys.data.local.entities.ConsultationEntity
import ucne.edu.ginecosys.data.local.entities.SyncStatus

@JsonClass(generateAdapter = true)
data class ConsultationDto(
    val id: String,
    @Json(name = "clinic_id") val clinicId: String,
    @Json(name = "patient_id") val patientId: String,
    val date: String,
    val type: String,
    val reason: String?,
    val diagnosis: String?,
    @Json(name = "doctor_name") val doctorName: String?,
    val plan: String?,
    val exam: String?,
    @Json(name = "vitals_bp") val vitalsBp: String?,
    @Json(name = "vitals_weight") val vitalsWeight: String?,
    @Json(name = "vitals_hr") val vitalsHr: String?,
    @Json(name = "vitals_temp") val vitalsTemp: String?,
    @Json(name = "vitals_fhr") val vitalsFhr: String?
)

fun ConsultationDto.toEntity(): ConsultationEntity {
    return ConsultationEntity(
        id = id,
        patientId = patientId,
        date = date,
        type = type,
        reason = reason ?: "",
        diagnosis = diagnosis ?: "",
        doctorName = doctorName,
        plan = plan,
        exam = exam,
        vitalsBp = vitalsBp,
        vitalsWeight = vitalsWeight,
        vitalsHr = vitalsHr,
        vitalsTemp = vitalsTemp,
        vitalsFhr = vitalsFhr,
        syncStatus = SyncStatus.SYNCED
    )
}

fun ConsultationEntity.toDto(clinicId: String): ConsultationDto {
    return ConsultationDto(
        id = id,
        clinicId = clinicId,
        patientId = patientId,
        date = date,
        type = type,
        reason = reason,
        diagnosis = diagnosis,
        doctorName = doctorName,
        plan = plan,
        exam = exam,
        vitalsBp = vitalsBp,
        vitalsWeight = vitalsWeight,
        vitalsHr = vitalsHr,
        vitalsTemp = vitalsTemp,
        vitalsFhr = vitalsFhr
    )
}
