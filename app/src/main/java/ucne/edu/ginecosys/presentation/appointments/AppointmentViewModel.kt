package ucne.edu.ginecosys.presentation.appointments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ucne.edu.ginecosys.domain.model.Appointment
import ucne.edu.ginecosys.domain.repository.AppointmentRepository
import ucne.edu.ginecosys.domain.repository.PatientRepository
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AppointmentViewModel @Inject constructor(
    private val repository: AppointmentRepository,
    private val patientRepository: PatientRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AppointmentUiState())
    val uiState: StateFlow<AppointmentUiState> = _uiState.asStateFlow()

    init {
        val cal = Calendar.getInstance()
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        _uiState.update {
            it.copy(
                selectedDate = today,
                formDate = today,
                currentYear = cal.get(Calendar.YEAR),
                currentMonth = cal.get(Calendar.MONTH)
            )
        }

        viewModelScope.launch {
            repository.getAppointments().collect { lists ->
                _uiState.update { state ->
                    state.copy(
                        appointments = lists,
                        appointmentsForSelectedDate = filterByDate(lists, state.selectedDate)
                    )
                }
            }
        }

        viewModelScope.launch {
            patientRepository.observePatients().collect { patientsList ->
                _uiState.update { state ->
                    state.copy(
                        patients = patientsList,
                        filteredPatients = filterPatients(patientsList, state.patientSearchQuery)
                    )
                }
            }
        }
    }

    fun onEvent(event: AppointmentEvent) {
        when (event) {
            is AppointmentEvent.SelectDate -> {
                _uiState.update { state ->
                    state.copy(
                        selectedDate = event.dateStr,
                        appointmentsForSelectedDate = filterByDate(state.appointments, event.dateStr)
                    )
                }
            }

            is AppointmentEvent.SubmitNewAppointment -> {
                viewModelScope.launch {
                    repository.addAppointment(event.appointment)
                }
            }

            is AppointmentEvent.NextMonth -> {
                _uiState.update { state ->
                    var newMonth = state.currentMonth + 1
                    var newYear = state.currentYear
                    if (newMonth > 11) { newMonth = 0; newYear++ }
                    state.copy(currentMonth = newMonth, currentYear = newYear)
                }
            }

            is AppointmentEvent.PrevMonth -> {
                _uiState.update { state ->
                    var newMonth = state.currentMonth - 1
                    var newYear = state.currentYear
                    if (newMonth < 0) { newMonth = 11; newYear-- }
                    state.copy(currentMonth = newMonth, currentYear = newYear)
                }
            }

            is AppointmentEvent.GoToToday -> {
                val cal = Calendar.getInstance()
                val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                _uiState.update { state ->
                    state.copy(
                        currentYear = cal.get(Calendar.YEAR),
                        currentMonth = cal.get(Calendar.MONTH),
                        selectedDate = today,
                        appointmentsForSelectedDate = filterByDate(state.appointments, today)
                    )
                }
            }

            is AppointmentEvent.SetViewMode -> {
                _uiState.update { it.copy(viewMode = event.mode) }
            }

            // Form events
            is AppointmentEvent.SearchPatient -> {
                _uiState.update { state ->
                    state.copy(
                        patientSearchQuery = event.query,
                        filteredPatients = filterPatients(state.patients, event.query)
                    )
                }
            }

            is AppointmentEvent.SelectPatient -> {
                _uiState.update { it.copy(selectedPatientId = event.id, selectedPatientName = event.name, formError = null) }
            }

            is AppointmentEvent.UpdateFormDate -> {
                _uiState.update { it.copy(formDate = event.date, formError = null) }
            }

            is AppointmentEvent.UpdateFormTime -> {
                _uiState.update { it.copy(formTime = event.time, formError = null) }
            }

            is AppointmentEvent.UpdateFormType -> {
                _uiState.update { it.copy(formType = event.type, formError = null) }
            }

            is AppointmentEvent.UpdateFormNotes -> {
                _uiState.update { it.copy(formNotes = event.notes) }
            }

            is AppointmentEvent.SaveAppointment -> saveAppointment()

            is AppointmentEvent.ResetForm -> {
                val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                _uiState.update {
                    it.copy(
                        selectedPatientId = null,
                        selectedPatientName = "",
                        patientSearchQuery = "",
                        formDate = today,
                        formTime = "09:00",
                        formType = "Consulta General",
                        formNotes = "",
                        formError = null,
                        appointmentSaved = false,
                        filteredPatients = it.patients
                    )
                }
            }
        }
    }

    private fun saveAppointment() {
        val state = _uiState.value

        if (state.selectedPatientId == null) {
            _uiState.update { it.copy(formError = "Seleccione un paciente") }
            return
        }
        if (state.formDate.isBlank()) {
            _uiState.update { it.copy(formError = "Seleccione una fecha") }
            return
        }

        val startTimeStr = "${state.formDate}T${state.formTime}:00"
        // End time = start + 30 mins
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val startDate = sdf.parse(startTimeStr)
        val endDate = Date((startDate?.time ?: 0) + 30 * 60 * 1000)
        val endTimeStr = sdf.format(endDate)

        val appointment = Appointment(
            id = UUID.randomUUID().toString(),
            patientId = state.selectedPatientId,
            patientName = state.selectedPatientName,
            startTime = startTimeStr,
            endTime = endTimeStr,
            type = state.formType,
            status = "scheduled",
            notes = state.formNotes.ifBlank { null }
        )

        viewModelScope.launch {
            repository.addAppointment(appointment)
            _uiState.update { it.copy(appointmentSaved = true) }
        }
    }

    private fun filterByDate(appointments: List<Appointment>, dateStr: String): List<Appointment> {
        if (dateStr.isBlank()) return emptyList()
        return appointments.filter { it.startTime.startsWith(dateStr) }
    }

    private fun filterPatients(patients: List<ucne.edu.ginecosys.domain.model.Patient>, query: String): List<ucne.edu.ginecosys.domain.model.Patient> {
        if (query.isBlank()) return patients
        return patients.filter {
            it.nombre.contains(query, ignoreCase = true) ||
            it.apellidos.contains(query, ignoreCase = true) ||
            it.cedula.contains(query, ignoreCase = true)
        }
    }
}
