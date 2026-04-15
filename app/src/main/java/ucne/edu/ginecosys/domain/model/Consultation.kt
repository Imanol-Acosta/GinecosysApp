package ucne.edu.ginecosys.domain.model

import java.util.UUID

data class Vitals(
    val bp: String = "",
    val weight: String = "",
    val hr: String = "",
    val temp: String = "",
    val fhr: String = ""
)

data class Consultation(
    val id: String = UUID.randomUUID().toString(),
    val patientId: String = "",
    val date: String = "",
    val type: String = "",
    val reason: String = "",
    val diagnosis: String = "",
    val doctorName: String = "",
    val plan: String = "",
    val exam: String = "",
    val vitals: Vitals? = null
)
