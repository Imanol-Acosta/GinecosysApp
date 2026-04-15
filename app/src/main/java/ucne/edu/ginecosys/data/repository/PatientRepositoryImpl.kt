package ucne.edu.ginecosys.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import ucne.edu.ginecosys.data.local.dao.PatientDao
import ucne.edu.ginecosys.data.local.entities.SyncStatus
import ucne.edu.ginecosys.data.local.entities.toDomain
import ucne.edu.ginecosys.data.local.entities.toEntity
import ucne.edu.ginecosys.data.remote.SupabasePatientApi
import ucne.edu.ginecosys.data.remote.dto.PatientDto
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
        // 1. Save locally
        val entity = patient.toEntity(SyncStatus.PENDING_INSERT)
        patientDao.insertPatient(entity)
        
        // 2. Try remote
        try {
            val clinicId = clinicRepository.getClinicId() ?: ""
            api.createPatient(entity.toDto(clinicId))
            patientDao.updateSyncStatus(patient.id, SyncStatus.SYNCED)
        } catch (e: Exception) {
            e.printStackTrace()
            // Stays PENDING_INSERT
        }
    }

    override suspend fun getPatientById(id: String): Patient? {
        return patientDao.getPatientById(id)?.toDomain()
    }

    override suspend fun updatePatientProfile(patient: Patient) {
        // 1. Update locally
        val entity = patient.toEntity(SyncStatus.PENDING_UPDATE)
        patientDao.updatePatient(entity)

        // 2. Try remote
        try {
            val clinicId = clinicRepository.getClinicId() ?: ""
            api.updatePatient("eq.${patient.id}", entity.toDto(clinicId))
            patientDao.updateSyncStatus(patient.id, SyncStatus.SYNCED)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun updatePatientFields(patientId: String, fields: Map<String, Any?>) {
        // 1. Update local entity with specific fields
        val existing = patientDao.getPatientById(patientId) ?: return
        val updated = existing.copy(
            fum = fields["fum"] as? String ?: existing.fum,
            cycle = fields["cycle"] as? String ?: existing.cycle,
            menarche = fields["menarche"] as? Int ?: existing.menarche,
            contraceptiveMethod = fields["contraceptive_method"] as? String ?: existing.contraceptiveMethod,
            sexualActivity = fields["sexual_activity"] as? Boolean ?: existing.sexualActivity,
            obstetricHistoryJson = if (fields.containsKey("obstetric_history")) {
                ucne.edu.ginecosys.data.local.Converters.obstetricHistoryToJson(
                    ucne.edu.ginecosys.data.local.Converters.mapToObstetricHistory(
                        @Suppress("UNCHECKED_CAST")
                        (fields["obstetric_history"] as? Map<String, Any>)
                    )
                )
            } else existing.obstetricHistoryJson,
            antecedentsJson = if (fields.containsKey("antecedents")) {
                ucne.edu.ginecosys.data.local.Converters.antecedentsToJson(
                    ucne.edu.ginecosys.data.local.Converters.mapToAntecedents(
                        @Suppress("UNCHECKED_CAST")
                        (fields["antecedents"] as? Map<String, Any>)
                    )
                )
            } else existing.antecedentsJson,
            activePregnancyJson = if (fields.containsKey("active_pregnancy")) {
                ucne.edu.ginecosys.data.local.Converters.activePregnancyToJson(
                    ucne.edu.ginecosys.data.local.Converters.mapToActivePregnancy(
                        @Suppress("UNCHECKED_CAST")
                        (fields["active_pregnancy"] as? Map<String, Any>)
                    )
                )
            } else existing.activePregnancyJson,
            pregnanciesJson = if (fields.containsKey("pregnancies")) {
                ucne.edu.ginecosys.data.local.Converters.pregnanciesToJson(
                    ucne.edu.ginecosys.data.local.Converters.mapListToPregnancies(
                        @Suppress("UNCHECKED_CAST")
                        (fields["pregnancies"] as? List<Map<String, Any>>)
                    )
                )
            } else existing.pregnanciesJson,
            syncStatus = SyncStatus.PENDING_UPDATE
        )
        patientDao.updatePatient(updated)

        // 2. Try remote partial update
        try {
            api.updatePatientFields("eq.$patientId", fields)
            patientDao.updateSyncStatus(patientId, SyncStatus.SYNCED)
        } catch (e: Exception) {
            e.printStackTrace()
        }
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
        val remotePatients = api.getPatients(eqClinicId = "eq.$clinicId")
        patientDao.insertPatients(remotePatients.map { it.toEntity() })
    }
}
