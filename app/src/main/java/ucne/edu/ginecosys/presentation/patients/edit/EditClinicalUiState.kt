package ucne.edu.ginecosys.presentation.patients.edit

data class EditClinicalUiState(
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val patientId: String = "",
    val fum: String = "",
    val ciclo: String = "",
    val menarquia: String = "",
    val planificacion: String = "",
    val sexualActivity: Boolean = false,
    val g: String = "0",
    val p: String = "0",
    val c: String = "0",
    val a: String = "0",
    val userMessage: String? = null,
    val savedSuccessfully: Boolean = false
)
