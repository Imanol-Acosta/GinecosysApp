package ucne.edu.ginecosys.domain.model

import java.util.UUID

data class Patient(
    val id: String = UUID.randomUUID().toString(),
    val nombre: String,
    val apellidos: String,
    val cedula: String = "",
    val fechaNacimiento: String,
    val ars: String = "",
    val direccion: String = "",
    val telefono: String = "",
    
    // Mock profile data for UI demonstration
    val bloodType: String = "O+",
    val gender: String = "Femenino",
    val insuranceType: String = if (ars.isNotBlank()) "Asegurado" else "Privado",
    
    // Ginecoloigcal profile mocks
    val fum: String = "--",
    val ciclo: String = "--",
    val menarquia: String = "--",
    val planificacion: String = "Ninguno",
    val gestaciones: Int = 0,
    val partos: Int = 0,
    val cesareas: Int = 0,
    val abortos: Int = 0
)

fun Patient.getAge(): Int {
    try {
        val parts = fechaNacimiento.split("/", "-", " ")
        for (part in parts) {
            if (part.length == 4) {
                return 2026 - part.toInt()
            }
        }
    } catch (e: Exception) {}
    return 25 // default fallback
}

fun Patient.getInitials(): String {
    val first = nombre.firstOrNull()?.uppercase() ?: ""
    val second = apellidos.firstOrNull()?.uppercase() ?: ""
    return "$first$second"
}
