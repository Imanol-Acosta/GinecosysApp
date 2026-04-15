package ucne.edu.ginecosys.presentation.insurance

import ucne.edu.ginecosys.domain.model.Insurance

sealed interface InsuranceEvent {
    data object LoadInsurances : InsuranceEvent
    data class UpdateName(val value: String) : InsuranceEvent
    data class UpdateType(val value: String) : InsuranceEvent
    data class UpdatePhone(val value: String) : InsuranceEvent
    data class ToggleActive(val insurance: Insurance) : InsuranceEvent
    data class DeleteInsurance(val id: String) : InsuranceEvent
    data class SelectForEdit(val insurance: Insurance?) : InsuranceEvent
    data object SaveInsurance : InsuranceEvent
    data object UserMessageShown : InsuranceEvent
    data object ShowAddDialog : InsuranceEvent
    data object HideDialog : InsuranceEvent
}

data class InsuranceUiState(
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val insurances: List<Insurance> = emptyList(),
    val showDialog: Boolean = false,
    val editingInsurance: Insurance? = null,
    val name: String = "",
    val type: String = "",
    val phone: String = "",
    val userMessage: String? = null
)
