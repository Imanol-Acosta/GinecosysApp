package ucne.edu.ginecosys.presentation.patients.edit

data class EditPatientUiState(
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val patientId: String = "",
    val nombre: String = "",
    val apellidos: String = "",
    val cedula: String = "",
    val fechaNacimiento: String = "",
    val genero: String = "Femenino",
    val telefono: String = "",
    val direccion: String = "",
    val ciudad: String = "",
    val ars: String = "",
    val bloodType: String = "",
    val civilStatus: String = "",
    val userMessage: String? = null,
    val savedSuccessfully: Boolean = false
)
