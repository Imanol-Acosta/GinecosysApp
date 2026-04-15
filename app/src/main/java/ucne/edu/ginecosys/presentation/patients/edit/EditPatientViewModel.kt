package ucne.edu.ginecosys.presentation.patients.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ucne.edu.ginecosys.domain.usecase.GetPatientByIdUseCase
import ucne.edu.ginecosys.domain.usecase.UpdatePatientProfileUseCase
import javax.inject.Inject

@HiltViewModel
class EditPatientViewModel @Inject constructor(
    private val getPatientByIdUseCase: GetPatientByIdUseCase,
    private val updatePatientProfileUseCase: UpdatePatientProfileUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(EditPatientUiState())
    val state: StateFlow<EditPatientUiState> = _state.asStateFlow()

    fun onEvent(event: EditPatientEvent) {
        when (event) {
            is EditPatientEvent.LoadPatient -> loadPatient(event.id)
            is EditPatientEvent.UpdateNombre -> _state.update { it.copy(nombre = event.value) }
            is EditPatientEvent.UpdateApellidos -> _state.update { it.copy(apellidos = event.value) }
            is EditPatientEvent.UpdateCedula -> _state.update { it.copy(cedula = event.value) }
            is EditPatientEvent.UpdateFechaNacimiento -> _state.update { it.copy(fechaNacimiento = event.value) }
            is EditPatientEvent.UpdateGenero -> _state.update { it.copy(genero = event.value) }
            is EditPatientEvent.UpdateTelefono -> _state.update { it.copy(telefono = event.value) }
            is EditPatientEvent.UpdateDireccion -> _state.update { it.copy(direccion = event.value) }
            is EditPatientEvent.UpdateCiudad -> _state.update { it.copy(ciudad = event.value) }
            is EditPatientEvent.UpdateArs -> _state.update { it.copy(ars = event.value) }
            is EditPatientEvent.UpdateBloodType -> _state.update { it.copy(bloodType = event.value) }
            is EditPatientEvent.UpdateCivilStatus -> _state.update { it.copy(civilStatus = event.value) }
            is EditPatientEvent.SavePatient -> savePatient()
            is EditPatientEvent.UserMessageShown -> _state.update { it.copy(userMessage = null) }
        }
    }

    private fun loadPatient(id: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val patient = getPatientByIdUseCase(id)
                if (patient != null) {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            patientId = patient.id,
                            nombre = patient.nombre,
                            apellidos = patient.apellidos,
                            cedula = patient.cedula,
                            fechaNacimiento = patient.fechaNacimiento,
                            genero = patient.gender,
                            telefono = patient.telefono,
                            direccion = patient.direccion,
                            ciudad = patient.ciudad,
                            ars = patient.ars,
                            bloodType = patient.bloodType,
                            civilStatus = patient.civilStatus
                        )
                    }
                } else {
                    _state.update { it.copy(isLoading = false, userMessage = "Paciente no encontrado") }
                }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, userMessage = "Error: ${e.localizedMessage}") }
            }
        }
    }

    private fun savePatient() {
        val s = _state.value
        if (s.nombre.isBlank() || s.apellidos.isBlank()) {
            _state.update { it.copy(userMessage = "Nombre y apellidos son requeridos") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isSaving = true) }
            try {
                val patient = getPatientByIdUseCase(s.patientId) ?: return@launch
                val updated = patient.copy(
                    nombre = s.nombre.trim(),
                    apellidos = s.apellidos.trim(),
                    cedula = s.cedula.trim(),
                    fechaNacimiento = s.fechaNacimiento.trim(),
                    gender = s.genero,
                    telefono = s.telefono.trim(),
                    direccion = s.direccion.trim(),
                    ciudad = s.ciudad.trim(),
                    ars = s.ars.trim(),
                    bloodType = s.bloodType.trim(),
                    civilStatus = s.civilStatus.trim()
                )
                updatePatientProfileUseCase(updated)
                _state.update { it.copy(isSaving = false, userMessage = "Perfil actualizado", savedSuccessfully = true) }
            } catch (e: Exception) {
                _state.update { it.copy(isSaving = false, userMessage = "Error: ${e.localizedMessage}") }
            }
        }
    }
}
