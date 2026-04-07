package ucne.edu.ginecosys.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import ucne.edu.ginecosys.data.local.dao.PatientDao
import ucne.edu.ginecosys.data.local.entities.SyncStatus
import ucne.edu.ginecosys.data.local.entities.toDomain
import ucne.edu.ginecosys.data.local.entities.toEntity
import ucne.edu.ginecosys.data.remote.SupabasePatientApi
import ucne.edu.ginecosys.data.remote.dto.toDto
import ucne.edu.ginecosys.data.remote.dto.toEntity
import ucne.edu.ginecosys.domain.model.Patient
import ucne.edu.ginecosys.domain.repository.PatientRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PatientRepositoryImpl @Inject constructor(
    private val patientDao: PatientDao,
    private val api: SupabasePatientApi,
    private val clinicRepository: ClinicRepository
) : PatientRepository {

    override fun observePatients(): Flow<List<Patient>> {
        return patientDao.observeAllPatients()
            .map { entities -> entities.map { it.toDomain() } }
            .onStart {
                try {
                    syncPatients()
                } catch (e: Exception) {
                    e.printStackTrace() // Ignore network errors for Offline-First
                }
            }
    }

    override suspend fun addPatient(patient: Patient) {
        // 1. Guardar localmente
        val entity = patient.toEntity(SyncStatus.PENDING_INSERT)
        patientDao.insertPatient(entity)
        
        // 2. Intentar enviar
        try {
            val clinicId = clinicRepository.getClinicId() ?: ""
            api.createPatient(entity.toDto(clinicId))
            patientDao.updateSyncStatus(patient.id, SyncStatus.SYNCED)
        } catch (e: Exception) {
            e.printStackTrace()
            // Se quedará PENDING_INSERT
        }
    }

    override suspend fun getPatientById(id: String): Patient? {
        return patientDao.getPatientById(id)?.toDomain()
    }

    private suspend fun syncPatients() {
        val clinicId = clinicRepository.getClinicId() ?: ""
        val unsynced = patientDao.getUnsyncedPatients()
        unsynced.forEach { entity ->
            try {
                when (entity.syncStatus) {
                    SyncStatus.PENDING_INSERT -> {
                        api.createPatient(entity.toDto(clinicId))
                        patientDao.updateSyncStatus(entity.id, SyncStatus.SYNCED)
                    }
                    SyncStatus.PENDING_UPDATE -> {
                        api.updatePatient("eq.${entity.id}", entity.toDto(clinicId))
                        patientDao.updateSyncStatus(entity.id, SyncStatus.SYNCED)
                    }
                    SyncStatus.PENDING_DELETE -> {
                        api.deletePatient("eq.${entity.id}")
                        patientDao.deletePatient(entity)
                    }
                    else -> {}
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        // Fetch remaining
        val remotePatients = api.getPatients()
        patientDao.insertPatients(remotePatients.map { it.toEntity() })
    }
}
