package ucne.edu.ginecosys.presentation.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ucne.edu.ginecosys.domain.repository.AppointmentRepository
import ucne.edu.ginecosys.domain.usecase.GetPatientByIdUseCase
import ucne.edu.ginecosys.domain.model.getFullName
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val appointmentRepository: AppointmentRepository,
    private val getPatientByIdUseCase: GetPatientByIdUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(CheckoutUiState())
    val state: StateFlow<CheckoutUiState> = _state.asStateFlow()

    fun onEvent(event: CheckoutEvent) {
        when (event) {
            is CheckoutEvent.LoadAppointment -> loadAppointment(event.appointmentId)
            is CheckoutEvent.UpdateAmount -> _state.update { it.copy(amount = event.value) }
            is CheckoutEvent.UpdateMethod -> _state.update { it.copy(method = event.value) }
            is CheckoutEvent.UpdateNotes -> _state.update { it.copy(notes = event.value) }
            is CheckoutEvent.ProcessPayment -> processPayment()
            is CheckoutEvent.UserMessageShown -> _state.update { it.copy(userMessage = null) }
        }
    }

    private fun loadAppointment(appointmentId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val appointment = appointmentRepository.getAppointmentById(appointmentId)
                if (appointment != null) {
                    val patient = getPatientByIdUseCase(appointment.patientId)
                    _state.update {
                        it.copy(
                            isLoading = false,
                            appointmentId = appointmentId,
                            patientName = patient?.getFullName() ?: "Paciente",
                            appointmentType = appointment.type,
                            appointmentDate = appointment.startTime
                        )
                    }
                } else {
                    _state.update { it.copy(isLoading = false, userMessage = "Cita no encontrada") }
                }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, userMessage = "Error: ${e.localizedMessage}") }
            }
        }
    }

    private fun processPayment() {
        val s = _state.value
        val amt = s.amount.toDoubleOrNull()
        if (amt == null || amt <= 0) {
            _state.update { it.copy(userMessage = "Ingresa un monto válido") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isProcessing = true) }
            try {
                // Mark appointment as completed
                val appointment = appointmentRepository.getAppointmentById(s.appointmentId)
                if (appointment != null) {
                    appointmentRepository.updateAppointment(
                        appointment.copy(status = "completed")
                    )
                }
                _state.update {
                    it.copy(
                        isProcessing = false,
                        userMessage = "Pago procesado: RD$ $amt",
                        paymentCompleted = true
                    )
                }
            } catch (e: Exception) {
                _state.update { it.copy(isProcessing = false, userMessage = "Error: ${e.localizedMessage}") }
            }
        }
    }
}
