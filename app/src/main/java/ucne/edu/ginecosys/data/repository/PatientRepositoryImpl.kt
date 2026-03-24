package ucne.edu.ginecosys.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ucne.edu.ginecosys.domain.model.Patient
import ucne.edu.ginecosys.domain.repository.PatientRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PatientRepositoryImpl @Inject constructor() : PatientRepository {
    // In-memory mock database representation 
    private val _patients = MutableStateFlow<List<Patient>>(emptyList())

    override fun observePatients(): Flow<List<Patient>> {
        return _patients.asStateFlow()
    }

    override suspend fun addPatient(patient: Patient) {
        _patients.update { it + patient }
    }

    override suspend fun getPatientById(id: String): Patient? {
        return _patients.value.find { it.id == id }
    }
}
