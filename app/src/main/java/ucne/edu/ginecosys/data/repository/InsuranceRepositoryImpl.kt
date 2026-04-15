package ucne.edu.ginecosys.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import ucne.edu.ginecosys.data.local.dao.InsuranceDao
import ucne.edu.ginecosys.data.local.entities.SyncStatus
import ucne.edu.ginecosys.data.local.entities.toDomain
import ucne.edu.ginecosys.data.local.entities.toEntity
import ucne.edu.ginecosys.data.remote.SupabaseInsuranceApi
import ucne.edu.ginecosys.data.remote.dto.toDto
import ucne.edu.ginecosys.data.remote.dto.toEntity
import ucne.edu.ginecosys.domain.model.Insurance
import ucne.edu.ginecosys.domain.repository.InsuranceRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InsuranceRepositoryImpl @Inject constructor(
    private val dao: InsuranceDao,
    private val api: SupabaseInsuranceApi,
    private val clinicRepository: ClinicRepository
) : InsuranceRepository {

    override fun observeAll(): Flow<List<Insurance>> {
        return dao.observeAll()
            .map { entities -> entities.map { it.toDomain() } }
            .onStart { try { sync() } catch (_: Exception) {} }
    }

    override suspend fun getById(id: String): Insurance? = dao.getById(id)?.toDomain()

    override suspend fun add(insurance: Insurance) {
        val entity = insurance.toEntity(SyncStatus.PENDING_INSERT)
        dao.insert(entity)
        try {
            val clinicId = clinicRepository.getClinicId() ?: ""
            api.createInsurance(entity.toDto(clinicId))
            dao.updateSyncStatus(insurance.id, SyncStatus.SYNCED)
        } catch (_: Exception) {}
    }

    override suspend fun update(insurance: Insurance) {
        val entity = insurance.toEntity(SyncStatus.PENDING_UPDATE)
        dao.update(entity)
        try {
            val clinicId = clinicRepository.getClinicId() ?: ""
            api.updateInsurance("eq.${insurance.id}", entity.toDto(clinicId))
            dao.updateSyncStatus(insurance.id, SyncStatus.SYNCED)
        } catch (_: Exception) {}
    }

    override suspend fun delete(id: String) {
        val entity = dao.getById(id) ?: return
        dao.update(entity.copy(syncStatus = SyncStatus.PENDING_DELETE))
        try {
            api.deleteInsurance("eq.$id")
            dao.delete(entity)
        } catch (_: Exception) {}
    }

    private suspend fun sync() {
        val clinicId = clinicRepository.getClinicId() ?: ""
        val unsynced = dao.getUnsynced()
        unsynced.forEach { entity ->
            try {
                when (entity.syncStatus) {
                    SyncStatus.PENDING_INSERT -> {
                        api.createInsurance(entity.toDto(clinicId))
                        dao.updateSyncStatus(entity.id, SyncStatus.SYNCED)
                    }
                    SyncStatus.PENDING_UPDATE -> {
                        api.updateInsurance("eq.${entity.id}", entity.toDto(clinicId))
                        dao.updateSyncStatus(entity.id, SyncStatus.SYNCED)
                    }
                    SyncStatus.PENDING_DELETE -> {
                        api.deleteInsurance("eq.${entity.id}")
                        dao.delete(entity)
                    }
                    else -> {}
                }
            } catch (_: Exception) {}
        }
        val remote = api.getInsurances()
        dao.insertAll(remote.map { it.toEntity() })
    }
}
