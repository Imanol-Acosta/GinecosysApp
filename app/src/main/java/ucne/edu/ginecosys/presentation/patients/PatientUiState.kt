package ucne.edu.ginecosys.presentation.patients

import ucne.edu.ginecosys.domain.model.Patient

data class PatientUiState(
    val isLoading: Boolean = false,
    val patients: List<Patient> = emptyList(),
    val filteredPatients: List<Patient> = emptyList(),
    val searchQuery: String = "",
    
    // Add Patient Form State
    val nombre: String = "",
    val apellidos: String = "",
    val cedula: String = "",
    val fechaNacimiento: String = "",
    val ars: String = "",
    val direccion: String = "",
    val telefono: String = "",
    val showError: Boolean = false,
    
    // Profile State
    val currentPatient: Patient? = null
)
