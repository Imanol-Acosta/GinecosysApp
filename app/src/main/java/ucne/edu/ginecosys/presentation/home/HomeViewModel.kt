package ucne.edu.ginecosys.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ucne.edu.ginecosys.domain.repository.AppointmentRepository
import ucne.edu.ginecosys.domain.repository.PatientRepository
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

data class HomeUiState(
    val selectedSegment: Int = 0,
    val isLoading: Boolean = false,
    val appointmentsToday: Int = 0,
    val totalPatients: Int = 0,
    val incomeMonth: String = "$0",
    val todayAppointmentsList: List<ucne.edu.ginecosys.domain.model.Appointment> = emptyList()
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val patientRepository: PatientRepository,
    private val appointmentRepository: AppointmentRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        val todayStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        viewModelScope.launch {
            patientRepository.observePatients().collect { patients ->
                _uiState.update { it.copy(totalPatients = patients.size) }
            }
        }

        viewModelScope.launch {
            appointmentRepository.getAppointments().collect { appointments ->
                val todayAppts = appointments.filter { it.startTime.startsWith(todayStr) }
                _uiState.update { 
                    it.copy(
                        appointmentsToday = todayAppts.size,
                        todayAppointmentsList = todayAppts,
                        // Ingresos we can mock for now as requested
                        incomeMonth = "$0" 
                    )
                }
            }
        }
    }

    fun setSegment(index: Int) {
        _uiState.update { it.copy(selectedSegment = index) }
    }
}
