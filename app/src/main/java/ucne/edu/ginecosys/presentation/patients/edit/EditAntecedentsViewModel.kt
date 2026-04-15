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
class EditAntecedentsViewModel @Inject constructor(
    private val getPatientByIdUseCase: GetPatientByIdUseCase,
    private val updatePatientFieldsUseCase: UpdatePatientFieldsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(EditAntecedentsUiState())
    val state: StateFlow<EditAntecedentsUiState> = _state.asStateFlow()

    fun onEvent(event: EditAntecedentsEvent) {
        when (event) {
            is EditAntecedentsEvent.LoadPatient -> loadPatient(event.id)
            is EditAntecedentsEvent.UpdateFamily -> _state.update { it.copy(family = event.value) }
            is EditAntecedentsEvent.UpdatePathological -> _state.update { it.copy(pathological = event.value) }
            is EditAntecedentsEvent.UpdateAllergies -> _state.update { it.copy(allergies = event.value) }
            is EditAntecedentsEvent.UpdateSurgical -> _state.update { it.copy(surgical = event.value) }
            is EditAntecedentsEvent.SaveAntecedents -> saveAntecedents()
            is EditAntecedentsEvent.UserMessageShown -> _state.update { it.copy(userMessage = null) }
        }
    }

    private fun loadPatient(id: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val patient = getPatientByIdUseCase(id)
                if (patient != null) {
                    val ant = patient.antecedents
                    _state.update {
                        it.copy(
                            isLoading = false,
                            patientId = patient.id,
                            family = ant?.family ?: "",
                            pathological = ant?.pathological ?: "",
                            allergies = ant?.allergies ?: "",
                            surgical = ant?.surgical ?: ""
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

    private fun saveAntecedents() {
        val s = _state.value
        viewModelScope.launch {
            _state.update { it.copy(isSaving = true) }
            try {
                val fields = mapOf<String, Any?>(
                    "antecedents" to mapOf(
                        "family" to s.family,
                        "pathological" to s.pathological,
                        "allergies" to s.allergies,
                        "surgical" to s.surgical
                    )
                )
                updatePatientFieldsUseCase(s.patientId, fields)
                _state.update { it.copy(isSaving = false, userMessage = "Antecedentes actualizados", savedSuccessfully = true) }
            } catch (e: Exception) {
                _state.update { it.copy(isSaving = false, userMessage = "Error: ${e.localizedMessage}") }
            }
        }
    }
}
