package ucne.edu.ginecosys.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ucne.edu.ginecosys.data.local.Converters
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
    @Json(name = "insurance_provider") val insuranceProvider: String?,

    // Expanded fields
    val gender: String? = null,
    val city: String? = null,
    @Json(name = "blood_type") val bloodType: String? = null,
    @Json(name = "civil_status") val civilStatus: String? = null,
    @Json(name = "insurance_id") val insuranceId: String? = null,
    val fum: String? = null,
    val cycle: String? = null,
    val menarche: Int? = null,
    @Json(name = "contraceptive_method") val contraceptiveMethod: String? = null,
    @Json(name = "sexual_activity") val sexualActivity: Boolean? = null,
    @Json(name = "obstetric_history") val obstetricHistory: Map<String, Any>? = null,
    val antecedents: Map<String, Any>? = null,
    @Json(name = "active_pregnancy") val activePregnancy: Map<String, Any>? = null,
    val pregnancies: List<Map<String, Any>>? = null
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
        gender = gender,
        city = city,
        bloodType = bloodType,
        civilStatus = civilStatus,
        insuranceId = insuranceId,
        fum = fum,
        cycle = cycle,
        menarche = menarche,
        contraceptiveMethod = contraceptiveMethod,
        sexualActivity = sexualActivity,
        obstetricHistoryJson = Converters.obstetricHistoryToJson(
            Converters.mapToObstetricHistory(obstetricHistory)
        ),
        antecedentsJson = Converters.antecedentsToJson(
            Converters.mapToAntecedents(antecedents)
        ),
        activePregnancyJson = Converters.activePregnancyToJson(
            Converters.mapToActivePregnancy(activePregnancy)
        ),
        pregnanciesJson = Converters.pregnanciesToJson(
            Converters.mapListToPregnancies(pregnancies)
        ),
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
        insuranceProvider = insuranceProvider,
        gender = gender,
        city = city,
        bloodType = bloodType,
        civilStatus = civilStatus,
        insuranceId = insuranceId,
        fum = fum,
        cycle = cycle,
        menarche = menarche,
        contraceptiveMethod = contraceptiveMethod,
        sexualActivity = sexualActivity,
        obstetricHistory = Converters.obstetricHistoryToMap(
            Converters.obstetricHistoryFromJson(obstetricHistoryJson)
        ),
        antecedents = Converters.antecedentsToMap(
            Converters.antecedentsFromJson(antecedentsJson)
        ),
        activePregnancy = Converters.activePregnancyToMap(
            Converters.activePregnancyFromJson(activePregnancyJson)
        ),
        pregnancies = Converters.pregnanciesListToMap(
            Converters.pregnanciesFromJson(pregnanciesJson)
        )
    )
}
