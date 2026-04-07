package ucne.edu.ginecosys.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import ucne.edu.ginecosys.data.local.dao.AppointmentDao
import ucne.edu.ginecosys.data.local.entities.AppointmentEntity
import ucne.edu.ginecosys.data.local.entities.SyncStatus
import ucne.edu.ginecosys.data.remote.SupabaseAppointmentApi
import ucne.edu.ginecosys.data.remote.dto.toDto
import ucne.edu.ginecosys.data.remote.dto.toEntity
import ucne.edu.ginecosys.domain.model.Appointment
import ucne.edu.ginecosys.domain.repository.AppointmentRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppointmentRepositoryImpl @Inject constructor(
    private val appointmentDao: AppointmentDao,
    private val api: SupabaseAppointmentApi,
    private val clinicRepository: ClinicRepository
) : AppointmentRepository {

    override fun getAppointments(): Flow<List<Appointment>> {
        return appointmentDao.getAppointments()
            .map { entities -> 
                entities.map { 
                    Appointment(
                        id = it.id,
                        patientId = it.patientId,
                        patientName = it.patientName,
                        startTime = it.startTime,
                        endTime = it.endTime,
                        type = it.type,
                        status = it.status,
                        notes = it.notes
                    ) 
                } 
            }
            .onStart {
                try {
                    syncAppointments()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
    }

    override suspend fun addAppointment(appointment: Appointment) {
        val entity = AppointmentEntity(
            id = appointment.id,
            patientId = appointment.patientId,
            patientName = appointment.patientName,
            startTime = appointment.startTime,
            endTime = appointment.endTime,
            type = appointment.type,
            status = appointment.status,
            notes = appointment.notes,
            syncStatus = SyncStatus.PENDING_INSERT
        )
        appointmentDao.insertAppointment(entity)
        
        try {
            val clinicId = clinicRepository.getClinicId() ?: ""
            api.insertAppointment(entity.toDto(clinicId))
            appointmentDao.updateSyncStatus(entity.id, SyncStatus.SYNCED)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun getAppointmentById(id: String): Appointment? {
        val entity = appointmentDao.getAppointmentById(id) ?: return null
        return Appointment(
            id = entity.id,
            patientId = entity.patientId,
            patientName = entity.patientName,
            startTime = entity.startTime,
            endTime = entity.endTime,
            type = entity.type,
            status = entity.status,
            notes = entity.notes
        )
    }

    override suspend fun updateAppointment(appointment: Appointment) {
        val entity = AppointmentEntity(
            id = appointment.id,
            patientId = appointment.patientId,
            patientName = appointment.patientName,
            startTime = appointment.startTime,
            endTime = appointment.endTime,
            type = appointment.type,
            status = appointment.status,
            notes = appointment.notes,
            syncStatus = SyncStatus.PENDING_UPDATE
        )
        appointmentDao.insertAppointment(entity)
        // Would add update mapping to API here
    }

    override suspend fun syncAppointments() {
        val unsynced = appointmentDao.getAppointmentsBySyncStatus(SyncStatus.PENDING_INSERT)
        val clinicId = clinicRepository.getClinicId() ?: ""
        
        unsynced.forEach { entity ->
            try {
                api.insertAppointment(entity.toDto(clinicId))
                appointmentDao.updateSyncStatus(entity.id, SyncStatus.SYNCED)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        // Fetch remote
        val remotes = api.getAppointments()
        appointmentDao.insertAppointments(remotes.map { it.toEntity() })
    }
}
