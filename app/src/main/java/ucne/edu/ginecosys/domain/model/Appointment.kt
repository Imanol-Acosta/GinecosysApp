package ucne.edu.ginecosys.domain.model

data class Appointment(
    val id: String,
    val patientId: String,
    val patientName: String,
    val startTime: String,
    val endTime: String,
    val type: String,
    val status: String,
    val notes: String? = null
)
