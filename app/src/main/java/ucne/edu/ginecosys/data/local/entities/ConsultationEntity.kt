package ucne.edu.ginecosys.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import ucne.edu.ginecosys.domain.model.Consultation
import ucne.edu.ginecosys.domain.model.Vitals

@Entity(tableName = "consultations")
data class ConsultationEntity(
    @PrimaryKey val id: String,
    val patientId: String,
    val date: String,
    val type: String,
    val reason: String,
    val diagnosis: String,
    val doctorName: String?,
    val plan: String?,
    val exam: String?,
    val vitalsBp: String?,
    val vitalsWeight: String?,
    val vitalsHr: String?,
    val vitalsTemp: String?,
    val vitalsFhr: String?,
    val syncStatus: SyncStatus = SyncStatus.PENDING_INSERT,
    val updatedAt: Long = System.currentTimeMillis()
)

fun ConsultationEntity.toDomain(): Consultation {
    return Consultation(
        id = id,
        patientId = patientId,
        date = date,
        type = type,
        reason = reason,
        diagnosis = diagnosis,
        doctorName = doctorName ?: "",
        plan = plan ?: "",
        exam = exam ?: "",
        vitals = Vitals(
            bp = vitalsBp ?: "",
            weight = vitalsWeight ?: "",
            hr = vitalsHr ?: "",
            temp = vitalsTemp ?: "",
            fhr = vitalsFhr ?: ""
        )
    )
}

fun Consultation.toEntity(syncStatus: SyncStatus = SyncStatus.PENDING_INSERT): ConsultationEntity {
    return ConsultationEntity(
        id = id,
        patientId = patientId,
        date = date,
        type = type,
        reason = reason,
        diagnosis = diagnosis,
        doctorName = doctorName,
        plan = plan,
        exam = exam,
        vitalsBp = vitals?.bp,
        vitalsWeight = vitals?.weight,
        vitalsHr = vitals?.hr,
        vitalsTemp = vitals?.temp,
        vitalsFhr = vitals?.fhr,
        syncStatus = syncStatus
    )
}
