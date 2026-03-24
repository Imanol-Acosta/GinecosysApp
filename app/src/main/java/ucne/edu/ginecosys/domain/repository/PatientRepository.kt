package ucne.edu.ginecosys.domain.repository

import kotlinx.coroutines.flow.Flow
import ucne.edu.ginecosys.domain.model.Patient

interface PatientRepository {
    fun observePatients(): Flow<List<Patient>>
    suspend fun addPatient(patient: Patient)
    suspend fun getPatientById(id: String): Patient?
}
