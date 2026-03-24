package ucne.edu.ginecosys.presentation.patients

sealed interface PatientEvent {
    data class UpdateNombre(val nombre: String) : PatientEvent
    data class UpdateApellidos(val apellidos: String) : PatientEvent
    data class UpdateCedula(val cedula: String) : PatientEvent
    data class UpdateFechaNacimiento(val fecha: String) : PatientEvent
    data class UpdateArs(val ars: String) : PatientEvent
    data class UpdateDireccion(val direccion: String) : PatientEvent
    data class UpdateTelefono(val telefono: String) : PatientEvent
    
    data object SavePatient : PatientEvent
    data class LoadPatient(val id: String) : PatientEvent
    data class SearchPatients(val query: String) : PatientEvent
    data object ClearCurrentPatient : PatientEvent
}
