package ucne.edu.ginecosys.domain.repository

import kotlinx.coroutines.flow.Flow
import ucne.edu.ginecosys.domain.model.Insurance

interface InsuranceRepository {
    fun observeAll(): Flow<List<Insurance>>
    suspend fun getById(id: String): Insurance?
    suspend fun add(insurance: Insurance)
    suspend fun update(insurance: Insurance)
    suspend fun delete(id: String)
}
