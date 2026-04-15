package ucne.edu.ginecosys.presentation.consultations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ucne.edu.ginecosys.domain.model.Consultation
import ucne.edu.ginecosys.domain.model.Vitals
import ucne.edu.ginecosys.domain.model.getFullName
import ucne.edu.ginecosys.domain.usecase.AddConsultationUseCase
import ucne.edu.ginecosys.domain.usecase.GetPatientByIdUseCase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class NewConsultationViewModel @Inject constructor(
    private val addConsultationUseCase: AddConsultationUseCase,
    private val getPatientByIdUseCase: GetPatientByIdUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ConsultationUiState())
    val state: StateFlow<ConsultationUiState> = _state.asStateFlow()

    fun onEvent(event: ConsultationEvent) {
        when (event) {
            is ConsultationEvent.LoadForPatient -> loadPatient(event.patientId)
            is ConsultationEvent.UpdateDate -> _state.update { it.copy(date = event.value) }
            is ConsultationEvent.UpdateType -> _state.update { it.copy(type = event.value) }
            is ConsultationEvent.UpdateReason -> _state.update { it.copy(reason = event.value) }
            is ConsultationEvent.UpdateDiagnosis -> _state.update { it.copy(diagnosis = event.value) }
            is ConsultationEvent.UpdatePlan -> _state.update { it.copy(plan = event.value) }
            is ConsultationEvent.UpdateExam -> _state.update { it.copy(exam = event.value) }
            is ConsultationEvent.UpdateVitalsBp -> _state.update { it.copy(vitals = it.vitals.copy(bp = event.value)) }
            is ConsultationEvent.UpdateVitalsWeight -> _state.update { it.copy(vitals = it.vitals.copy(weight = event.value)) }
            is ConsultationEvent.UpdateVitalsHr -> _state.update { it.copy(vitals = it.vitals.copy(hr = event.value)) }
            is ConsultationEvent.UpdateVitalsTemp -> _state.update { it.copy(vitals = it.vitals.copy(temp = event.value)) }
            is ConsultationEvent.UpdateVitalsFhr -> _state.update { it.copy(vitals = it.vitals.copy(fhr = event.value)) }
            is ConsultationEvent.SaveConsultation -> save()
            is ConsultationEvent.UserMessageShown -> _state.update { it.copy(userMessage = null) }
        }
    }

    private fun loadPatient(patientId: String) {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val patient = getPatientByIdUseCase(patientId)
                _state.update {
                    it.copy(
                        isLoading = false,
                        patientId = patientId,
                        patientName = patient?.getFullName() ?: "",
                        date = today
                    )
                }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, date = today, patientId = patientId) }
            }
        }
    }

    private fun save() {
        val s = _state.value
        if (s.reason.isBlank()) {
            _state.update { it.copy(userMessage = "El motivo de consulta es requerido") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isSaving = true) }
            try {
                val consultation = Consultation(
                    patientId = s.patientId,
                    date = s.date,
                    type = s.type,
                    reason = s.reason.trim(),
                    diagnosis = s.diagnosis.trim(),
                    plan = s.plan.trim(),
                    exam = s.exam.trim(),
                    vitals = s.vitals
                )
                addConsultationUseCase(consultation)
                _state.update { it.copy(isSaving = false, userMessage = "Consulta registrada", savedSuccessfully = true) }
            } catch (e: Exception) {
                _state.update { it.copy(isSaving = false, userMessage = "Error: ${e.localizedMessage}") }
            }
        }
    }
}
