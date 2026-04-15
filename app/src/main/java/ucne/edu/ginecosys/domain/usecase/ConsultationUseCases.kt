package ucne.edu.ginecosys.domain.usecase

import kotlinx.coroutines.flow.Flow
import ucne.edu.ginecosys.domain.model.Consultation
import ucne.edu.ginecosys.domain.repository.ConsultationRepository
import javax.inject.Inject

class ObserveConsultationsByPatientUseCase @Inject constructor(
    private val repository: ConsultationRepository
) {
    operator fun invoke(patientId: String): Flow<List<Consultation>> =
        repository.observeByPatient(patientId)
}

class ObserveAllConsultationsUseCase @Inject constructor(
    private val repository: ConsultationRepository
) {
    operator fun invoke(): Flow<List<Consultation>> = repository.observeAll()
}

class AddConsultationUseCase @Inject constructor(
    private val repository: ConsultationRepository
) {
    suspend operator fun invoke(consultation: Consultation) =
        repository.addConsultation(consultation)
}

class GetConsultationByIdUseCase @Inject constructor(
    private val repository: ConsultationRepository
) {
    suspend operator fun invoke(id: String): Consultation? = repository.getById(id)
}

class UpdateConsultationUseCase @Inject constructor(
    private val repository: ConsultationRepository
) {
    suspend operator fun invoke(consultation: Consultation) =
        repository.updateConsultation(consultation)
}

class DeleteConsultationUseCase @Inject constructor(
    private val repository: ConsultationRepository
) {
    suspend operator fun invoke(id: String) = repository.deleteConsultation(id)
}
