package ucne.edu.ginecosys.domain.usecase

import kotlinx.coroutines.flow.Flow
import ucne.edu.ginecosys.domain.model.Patient
import ucne.edu.ginecosys.domain.repository.PatientRepository
import javax.inject.Inject

class ObservePatientsUseCase @Inject constructor(
    private val repository: PatientRepository
) {
    operator fun invoke(): Flow<List<Patient>> = repository.observePatients()
}

class AddPatientUseCase @Inject constructor(
    private val repository: PatientRepository
) {
    suspend operator fun invoke(patient: Patient) {
        repository.addPatient(patient)
    }
}

class GetPatientByIdUseCase @Inject constructor(
    private val repository: PatientRepository
) {
    suspend operator fun invoke(id: String): Patient? {
        return repository.getPatientById(id)
    }
}

class UpdatePatientProfileUseCase @Inject constructor(
    private val repository: PatientRepository
) {
    suspend operator fun invoke(patient: Patient) {
        repository.updatePatientProfile(patient)
    }
}

class UpdatePatientFieldsUseCase @Inject constructor(
    private val repository: PatientRepository
) {
    suspend operator fun invoke(patientId: String, fields: Map<String, Any?>) {
        repository.updatePatientFields(patientId, fields)
    }
}
