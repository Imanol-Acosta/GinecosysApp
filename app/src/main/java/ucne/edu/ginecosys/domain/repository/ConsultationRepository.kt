package ucne.edu.ginecosys.domain.repository

import kotlinx.coroutines.flow.Flow
import ucne.edu.ginecosys.domain.model.Consultation

interface ConsultationRepository {
    fun observeByPatient(patientId: String): Flow<List<Consultation>>
    fun observeAll(): Flow<List<Consultation>>
    suspend fun getById(id: String): Consultation?
    suspend fun addConsultation(consultation: Consultation)
    suspend fun updateConsultation(consultation: Consultation)
    suspend fun deleteConsultation(id: String)
}
