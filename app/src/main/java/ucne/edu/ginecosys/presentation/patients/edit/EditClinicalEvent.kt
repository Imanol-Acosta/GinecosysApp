package ucne.edu.ginecosys.presentation.patients.edit

sealed interface EditClinicalEvent {
    data class LoadPatient(val id: String) : EditClinicalEvent
    data class UpdateFum(val value: String) : EditClinicalEvent
    data class UpdateCiclo(val value: String) : EditClinicalEvent
    data class UpdateMenarquia(val value: String) : EditClinicalEvent
    data class UpdatePlanificacion(val value: String) : EditClinicalEvent
    data class UpdateSexualActivity(val value: Boolean) : EditClinicalEvent
    data class UpdateG(val value: String) : EditClinicalEvent
    data class UpdateP(val value: String) : EditClinicalEvent
    data class UpdateC(val value: String) : EditClinicalEvent
    data class UpdateA(val value: String) : EditClinicalEvent
    data object SaveClinical : EditClinicalEvent
    data object UserMessageShown : EditClinicalEvent
}
