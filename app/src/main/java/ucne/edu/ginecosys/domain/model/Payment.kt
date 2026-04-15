package ucne.edu.ginecosys.domain.model

import java.util.UUID

data class Payment(
    val id: String = UUID.randomUUID().toString(),
    val appointmentId: String = "",
    val patientId: String = "",
    val amount: Double = 0.0,
    val method: String = "Efectivo",
    val status: String = "Pendiente",
    val date: String = "",
    val notes: String = ""
)
