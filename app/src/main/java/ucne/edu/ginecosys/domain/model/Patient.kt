package ucne.edu.ginecosys.domain.model

import java.util.UUID

// Sub-models for gynecological data
data class ObstetricHistory(
    val g: Int = 0,
    val p: Int = 0,
    val c: Int = 0,
    val a: Int = 0
)

data class Antecedents(
    val family: String = "",
    val pathological: String = "",
    val allergies: String = "",
    val surgical: String = ""
)

data class ActivePregnancy(
    val weeks: Int = 0,
    val fpp: String = "",
    val risk: String = "Bajo",
    val lastFHR: String = ""
)

data class Pregnancy(
    val id: String = UUID.randomUUID().toString(),
    val year: Int = 0,
    val outcome: String = "",
    val details: String = ""
)

data class Patient(
    val id: String = UUID.randomUUID().toString(),
    val nombre: String,
    val apellidos: String,
    val cedula: String = "",
    val fechaNacimiento: String = "",
    val gender: String = "Femenino",
    val ars: String = "",
    val insuranceId: String = "",
    val direccion: String = "",
    val ciudad: String = "",
    val telefono: String = "",
    val bloodType: String = "",
    val civilStatus: String = "",

    // Gynecological profile (real data from Supabase)
    val fum: String = "",
    val ciclo: String = "",
    val menarquia: Int? = null,
    val planificacion: String = "",
    val sexualActivity: Boolean? = null,
    val obstetricHistory: ObstetricHistory? = null,
    val antecedents: Antecedents? = null,
    val activePregnancy: ActivePregnancy? = null,
    val pregnancies: List<Pregnancy> = emptyList()
)

fun Patient.getAge(): Int {
    try {
        val parts = fechaNacimiento.split("/", "-", " ")
        for (part in parts) {
            if (part.length == 4) {
                val currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)
                return currentYear - part.toInt()
            }
        }
    } catch (_: Exception) {}
    return 0
}

fun Patient.getInitials(): String {
    val first = nombre.firstOrNull()?.uppercase() ?: ""
    val second = apellidos.firstOrNull()?.uppercase() ?: ""
    return "$first$second"
}

fun Patient.getFullName(): String = "$nombre $apellidos"
