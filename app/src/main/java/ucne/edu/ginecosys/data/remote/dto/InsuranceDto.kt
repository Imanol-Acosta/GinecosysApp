package ucne.edu.ginecosys.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ucne.edu.ginecosys.data.local.entities.InsuranceEntity
import ucne.edu.ginecosys.data.local.entities.SyncStatus

@JsonClass(generateAdapter = true)
data class InsuranceDto(
    val id: String,
    @Json(name = "clinic_id") val clinicId: String,
    val name: String,
    val type: String,
    val phone: String?,
    @Json(name = "is_active") val isActive: Boolean = true
)

fun InsuranceDto.toEntity(): InsuranceEntity = InsuranceEntity(
    id = id, name = name, type = type, phone = phone, isActive = isActive, syncStatus = SyncStatus.SYNCED
)

fun InsuranceEntity.toDto(clinicId: String): InsuranceDto = InsuranceDto(
    id = id, clinicId = clinicId, name = name, type = type, phone = phone, isActive = isActive
)
