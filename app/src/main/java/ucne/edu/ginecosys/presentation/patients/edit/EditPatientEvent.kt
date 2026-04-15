package ucne.edu.ginecosys.presentation.patients.edit

sealed interface EditPatientEvent {
    data class UpdateNombre(val value: String) : EditPatientEvent
    data class UpdateApellidos(val value: String) : EditPatientEvent
    data class UpdateCedula(val value: String) : EditPatientEvent
    data class UpdateFechaNacimiento(val value: String) : EditPatientEvent
    data class UpdateGenero(val value: String) : EditPatientEvent
    data class UpdateTelefono(val value: String) : EditPatientEvent
    data class UpdateDireccion(val value: String) : EditPatientEvent
    data class UpdateCiudad(val value: String) : EditPatientEvent
    data class UpdateArs(val value: String) : EditPatientEvent
    data class UpdateBloodType(val value: String) : EditPatientEvent
    data class UpdateCivilStatus(val value: String) : EditPatientEvent
    data class LoadPatient(val id: String) : EditPatientEvent
    data object SavePatient : EditPatientEvent
    data object UserMessageShown : EditPatientEvent
}
