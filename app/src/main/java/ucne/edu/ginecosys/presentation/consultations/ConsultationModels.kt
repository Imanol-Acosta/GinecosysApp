package ucne.edu.ginecosys.presentation.consultations

import ucne.edu.ginecosys.domain.model.Vitals

sealed interface ConsultationEvent {
    data class LoadForPatient(val patientId: String) : ConsultationEvent
    data class UpdateDate(val value: String) : ConsultationEvent
    data class UpdateType(val value: String) : ConsultationEvent
    data class UpdateReason(val value: String) : ConsultationEvent
    data class UpdateDiagnosis(val value: String) : ConsultationEvent
    data class UpdatePlan(val value: String) : ConsultationEvent
    data class UpdateExam(val value: String) : ConsultationEvent
    data class UpdateVitalsBp(val value: String) : ConsultationEvent
    data class UpdateVitalsWeight(val value: String) : ConsultationEvent
    data class UpdateVitalsHr(val value: String) : ConsultationEvent
    data class UpdateVitalsTemp(val value: String) : ConsultationEvent
    data class UpdateVitalsFhr(val value: String) : ConsultationEvent
    data object SaveConsultation : ConsultationEvent
    data object UserMessageShown : ConsultationEvent
}

data class ConsultationUiState(
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val patientId: String = "",
    val patientName: String = "",
    val date: String = "",
    val type: String = "General",
    val reason: String = "",
    val diagnosis: String = "",
    val plan: String = "",
    val exam: String = "",
    val vitals: Vitals = Vitals(),
    val userMessage: String? = null,
    val savedSuccessfully: Boolean = false
)
