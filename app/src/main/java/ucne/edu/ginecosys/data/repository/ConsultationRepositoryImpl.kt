package ucne.edu.ginecosys.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import ucne.edu.ginecosys.data.local.dao.ConsultationDao
import ucne.edu.ginecosys.data.local.entities.SyncStatus
import ucne.edu.ginecosys.data.local.entities.toDomain
import ucne.edu.ginecosys.data.local.entities.toEntity
import ucne.edu.ginecosys.data.remote.SupabaseConsultationApi
import ucne.edu.ginecosys.data.remote.dto.toDto
import ucne.edu.ginecosys.data.remote.dto.toEntity
import ucne.edu.ginecosys.domain.model.Consultation
import ucne.edu.ginecosys.domain.repository.ConsultationRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConsultationRepositoryImpl @Inject constructor(
    private val dao: ConsultationDao,
    private val api: SupabaseConsultationApi,
    private val clinicRepository: ClinicRepository
) : ConsultationRepository {

    override fun observeByPatient(patientId: String): Flow<List<Consultation>> {
        return dao.observeByPatient(patientId)
            .map { entities -> entities.map { it.toDomain() } }
            .onStart {
                try { syncConsultations() } catch (_: Exception) {}
            }
    }

    override fun observeAll(): Flow<List<Consultation>> {
        return dao.observeAll()
            .map { entities -> entities.map { it.toDomain() } }
            .onStart {
                try { syncConsultations() } catch (_: Exception) {}
            }
    }

    override suspend fun getById(id: String): Consultation? {
        return dao.getById(id)?.toDomain()
    }

    override suspend fun addConsultation(consultation: Consultation) {
        val entity = consultation.toEntity(SyncStatus.PENDING_INSERT)
        dao.insert(entity)

        try {
            val clinicId = clinicRepository.getClinicId() ?: ""
            api.createConsultation(entity.toDto(clinicId))
            dao.updateSyncStatus(consultation.id, SyncStatus.SYNCED)
        } catch (_: Exception) {}
    }

    override suspend fun updateConsultation(consultation: Consultation) {
        val entity = consultation.toEntity(SyncStatus.PENDING_UPDATE)
        dao.update(entity)

        try {
            val clinicId = clinicRepository.getClinicId() ?: ""
            api.updateConsultation("eq.${consultation.id}", entity.toDto(clinicId))
            dao.updateSyncStatus(consultation.id, SyncStatus.SYNCED)
        } catch (_: Exception) {}
    }

    override suspend fun deleteConsultation(id: String) {
        val entity = dao.getById(id) ?: return
        val marked = entity.copy(syncStatus = SyncStatus.PENDING_DELETE)
        dao.update(marked)

        try {
            api.deleteConsultation("eq.$id")
            dao.delete(entity)
        } catch (_: Exception) {}
    }

    private suspend fun syncConsultations() {
        val clinicId = clinicRepository.getClinicId() ?: ""
        val unsynced = dao.getUnsynced()
        unsynced.forEach { entity ->
            try {
                when (entity.syncStatus) {
                    SyncStatus.PENDING_INSERT -> {
                        api.createConsultation(entity.toDto(clinicId))
                        dao.updateSyncStatus(entity.id, SyncStatus.SYNCED)
                    }
                    SyncStatus.PENDING_UPDATE -> {
                        api.updateConsultation("eq.${entity.id}", entity.toDto(clinicId))
                        dao.updateSyncStatus(entity.id, SyncStatus.SYNCED)
                    }
                    SyncStatus.PENDING_DELETE -> {
                        api.deleteConsultation("eq.${entity.id}")
                        dao.delete(entity)
                    }
                    else -> {}
                }
            } catch (_: Exception) {}
        }

        // Updated: Removed clinicId filter as the column doesn't exist in Supabase table 'consultations'
        val remote = api.getConsultations()
        val unsyncedIds = dao.getUnsyncedIds().toSet()
        val toInsert = remote
            .map { it.toEntity() }
            .filter { it.id !in unsyncedIds }

        dao.insertAll(toInsert)
    }
}
