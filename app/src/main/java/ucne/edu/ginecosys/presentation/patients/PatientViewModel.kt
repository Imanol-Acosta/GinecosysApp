package ucne.edu.ginecosys.presentation.patients

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ucne.edu.ginecosys.domain.model.Patient
import ucne.edu.ginecosys.domain.usecase.AddPatientUseCase
import ucne.edu.ginecosys.domain.usecase.GetPatientByIdUseCase
import ucne.edu.ginecosys.domain.usecase.ObservePatientsUseCase
import javax.inject.Inject

@HiltViewModel
class PatientViewModel @Inject constructor(
    private val observePatientsUseCase: ObservePatientsUseCase,
    private val addPatientUseCase: AddPatientUseCase,
    private val getPatientByIdUseCase: GetPatientByIdUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(PatientUiState())
    val state: StateFlow<PatientUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            observePatientsUseCase().collect { patientsList ->
                _state.update { it.copy(
                    patients = patientsList,
                    filteredPatients = filterList(patientsList, it.searchQuery)
                ) }
            }
        }
    }

    fun onEvent(event: PatientEvent) {
        when (event) {
            is PatientEvent.UpdateNombre -> _state.update { it.copy(nombre = event.nombre, showError = false) }
            is PatientEvent.UpdateApellidos -> _state.update { it.copy(apellidos = event.apellidos, showError = false) }
            is PatientEvent.UpdateCedula -> _state.update { it.copy(cedula = event.cedula) }
            is PatientEvent.UpdateFechaNacimiento -> _state.update { it.copy(fechaNacimiento = event.fecha, showError = false) }
            is PatientEvent.UpdateArs -> _state.update { it.copy(ars = event.ars) }
            is PatientEvent.UpdateDireccion -> _state.update { it.copy(direccion = event.direccion) }
            is PatientEvent.UpdateTelefono -> _state.update { it.copy(telefono = event.telefono) }
            
            is PatientEvent.SavePatient -> savePatient()
            is PatientEvent.LoadPatient -> loadPatient(event.id)
            is PatientEvent.SearchPatients -> {
                _state.update { it.copy(
                    searchQuery = event.query,
                    filteredPatients = filterList(it.patients, event.query)
                ) }
            }
            is PatientEvent.ClearCurrentPatient -> {
                _state.update { it.copy(
                    currentPatient = null,
                    nombre = "", apellidos = "", cedula = "", fechaNacimiento = "",
                    ars = "", direccion = "", telefono = "", showError = false
                ) }
            }
        }
    }

    private fun savePatient() {
        val currentState = _state.value
        if (currentState.nombre.isBlank() || currentState.apellidos.isBlank() || currentState.fechaNacimiento.isBlank()) {
            _state.update { it.copy(showError = true) }
            return
        }
        
        viewModelScope.launch {
            val newPatient = Patient(
                nombre = currentState.nombre.trim(),
                apellidos = currentState.apellidos.trim(),
                cedula = currentState.cedula.trim(),
                fechaNacimiento = currentState.fechaNacimiento.trim(),
                ars = currentState.ars.trim(),
                direccion = currentState.direccion.trim(),
                telefono = currentState.telefono.trim()
            )
            addPatientUseCase(newPatient)
            onEvent(PatientEvent.ClearCurrentPatient)
        }
    }

    private fun loadPatient(id: String) {
        viewModelScope.launch {
            val patient = getPatientByIdUseCase(id)
            _state.update { it.copy(currentPatient = patient) }
        }
    }
    
    private fun filterList(patients: List<Patient>, query: String): List<Patient> {
        if (query.isBlank()) return patients
        return patients.filter {
            it.nombre.contains(query, ignoreCase = true) ||
            it.apellidos.contains(query, ignoreCase = true) ||
            it.cedula.contains(query, ignoreCase = true)
        }
    }
}
