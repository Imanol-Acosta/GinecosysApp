package ucne.edu.ginecosys.domain.repository

import kotlinx.coroutines.flow.Flow
import ucne.edu.ginecosys.domain.model.Appointment

interface AppointmentRepository {
    fun getAppointments(): Flow<List<Appointment>>
    suspend fun addAppointment(appointment: Appointment)
    suspend fun getAppointmentById(id: String): Appointment?
    suspend fun updateAppointment(appointment: Appointment)
    suspend fun syncAppointments()
}
