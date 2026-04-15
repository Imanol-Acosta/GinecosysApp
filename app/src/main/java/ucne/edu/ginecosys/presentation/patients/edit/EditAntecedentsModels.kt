package ucne.edu.ginecosys.presentation.patients.edit

sealed interface EditAntecedentsEvent {
    data class LoadPatient(val id: String) : EditAntecedentsEvent
    data class UpdateFamily(val value: String) : EditAntecedentsEvent
    data class UpdatePathological(val value: String) : EditAntecedentsEvent
    data class UpdateAllergies(val value: String) : EditAntecedentsEvent
    data class UpdateSurgical(val value: String) : EditAntecedentsEvent
    data object SaveAntecedents : EditAntecedentsEvent
    data object UserMessageShown : EditAntecedentsEvent
}

data class EditAntecedentsUiState(
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val patientId: String = "",
    val family: String = "",
    val pathological: String = "",
    val allergies: String = "",
    val surgical: String = "",
    val userMessage: String? = null,
    val savedSuccessfully: Boolean = false
)
