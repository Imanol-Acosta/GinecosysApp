package ucne.edu.ginecosys.presentation.appointments

import ucne.edu.ginecosys.domain.model.Appointment
import ucne.edu.ginecosys.domain.model.Patient

enum class CalendarViewMode { MONTHLY, WEEKLY }

data class AppointmentUiState(
    val appointments: List<Appointment> = emptyList(),
    val appointmentsForSelectedDate: List<Appointment> = emptyList(),
    val selectedDate: String = "", // YYYY-MM-DD
    val isLoading: Boolean = false,
    val error: String? = null,

    // Calendar navigation
    val viewMode: CalendarViewMode = CalendarViewMode.MONTHLY,
    val currentYear: Int = 0,
    val currentMonth: Int = 0, // 0-indexed (Jan=0)

    // Add Appointment Form
    val patients: List<Patient> = emptyList(),
    val filteredPatients: List<Patient> = emptyList(),
    val patientSearchQuery: String = "",
    val selectedPatientId: String? = null,
    val selectedPatientName: String = "",
    val formDate: String = "",
    val formTime: String = "09:00",
    val formType: String = "Consulta General",
    val formNotes: String = "",
    val formError: String? = null,
    val appointmentSaved: Boolean = false
)

sealed class AppointmentEvent {
    data class SelectDate(val dateStr: String) : AppointmentEvent()
    data class SubmitNewAppointment(val appointment: Appointment) : AppointmentEvent()

    // Calendar navigation
    data object NextMonth : AppointmentEvent()
    data object PrevMonth : AppointmentEvent()
    data object GoToToday : AppointmentEvent()
    data class SetViewMode(val mode: CalendarViewMode) : AppointmentEvent()

    // Add appointment form
    data class SearchPatient(val query: String) : AppointmentEvent()
    data class SelectPatient(val id: String, val name: String) : AppointmentEvent()
    data class UpdateFormDate(val date: String) : AppointmentEvent()
    data class UpdateFormTime(val time: String) : AppointmentEvent()
    data class UpdateFormType(val type: String) : AppointmentEvent()
    data class UpdateFormNotes(val notes: String) : AppointmentEvent()
    data object SaveAppointment : AppointmentEvent()
    data object ResetForm : AppointmentEvent()
}
