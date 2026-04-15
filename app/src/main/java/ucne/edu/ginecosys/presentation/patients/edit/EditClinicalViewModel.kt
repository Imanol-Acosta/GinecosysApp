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
import ucne.edu.ginecosys.domain.usecase.UpdatePatientFieldsUseCase
import javax.inject.Inject

@HiltViewModel
class EditClinicalViewModel @Inject constructor(
    private val getPatientByIdUseCase: GetPatientByIdUseCase,
    private val updatePatientFieldsUseCase: UpdatePatientFieldsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(EditClinicalUiState())
    val state: StateFlow<EditClinicalUiState> = _state.asStateFlow()

    fun onEvent(event: EditClinicalEvent) {
        when (event) {
            is EditClinicalEvent.LoadPatient -> loadPatient(event.id)
            is EditClinicalEvent.UpdateFum -> _state.update { it.copy(fum = event.value) }
            is EditClinicalEvent.UpdateCiclo -> _state.update { it.copy(ciclo = event.value) }
            is EditClinicalEvent.UpdateMenarquia -> _state.update { it.copy(menarquia = event.value) }
            is EditClinicalEvent.UpdatePlanificacion -> _state.update { it.copy(planificacion = event.value) }
            is EditClinicalEvent.UpdateSexualActivity -> _state.update { it.copy(sexualActivity = event.value) }
            is EditClinicalEvent.UpdateG -> _state.update { it.copy(g = event.value) }
            is EditClinicalEvent.UpdateP -> _state.update { it.copy(p = event.value) }
            is EditClinicalEvent.UpdateC -> _state.update { it.copy(c = event.value) }
            is EditClinicalEvent.UpdateA -> _state.update { it.copy(a = event.value) }
            is EditClinicalEvent.SaveClinical -> saveClinical()
            is EditClinicalEvent.UserMessageShown -> _state.update { it.copy(userMessage = null) }
        }
    }

    private fun loadPatient(id: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val patient = getPatientByIdUseCase(id)
                if (patient != null) {
                    val obs = patient.obstetricHistory
                    _state.update {
                        it.copy(
                            isLoading = false,
                            patientId = patient.id,
                            fum = patient.fum,
                            ciclo = patient.ciclo,
                            menarquia = patient.menarquia?.toString() ?: "",
                            planificacion = patient.planificacion,
                            sexualActivity = patient.sexualActivity ?: false,
                            g = (obs?.g ?: 0).toString(),
                            p = (obs?.p ?: 0).toString(),
                            c = (obs?.c ?: 0).toString(),
                            a = (obs?.a ?: 0).toString()
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

    private fun saveClinical() {
        val s = _state.value
        viewModelScope.launch {
            _state.update { it.copy(isSaving = true) }
            try {
                val fields = mutableMapOf<String, Any?>(
                    "fum" to s.fum.ifBlank { null },
                    "cycle" to s.ciclo.ifBlank { null },
                    "menarche" to s.menarquia.toIntOrNull(),
                    "contraceptive_method" to s.planificacion.ifBlank { null },
                    "sexual_activity" to s.sexualActivity,
                    "obstetric_history" to mapOf(
                        "g" to (s.g.toIntOrNull() ?: 0),
                        "p" to (s.p.toIntOrNull() ?: 0),
                        "c" to (s.c.toIntOrNull() ?: 0),
                        "a" to (s.a.toIntOrNull() ?: 0)
                    )
                )
                updatePatientFieldsUseCase(s.patientId, fields)
                _state.update { it.copy(isSaving = false, userMessage = "Datos clínicos actualizados", savedSuccessfully = true) }
            } catch (e: Exception) {
                _state.update { it.copy(isSaving = false, userMessage = "Error: ${e.localizedMessage}") }
            }
        }
    }
}
