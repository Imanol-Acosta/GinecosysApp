package ucne.edu.ginecosys.presentation.records

import ucne.edu.ginecosys.domain.model.Consultation
import ucne.edu.ginecosys.domain.model.Patient

data class RecordsUiState(
    val isLoading: Boolean = false,
    val consultations: List<Consultation> = emptyList(),
    val patients: Map<String, Patient> = emptyMap(),
    val searchQuery: String = "",
    val filterType: String = ""
) {
    val filteredConsultations: List<Consultation>
        get() {
            var result = consultations
            if (filterType.isNotBlank()) {
                result = result.filter { it.type.equals(filterType, ignoreCase = true) }
            }
            if (searchQuery.isNotBlank()) {
                val q = searchQuery.lowercase()
                result = result.filter { c ->
                    val patient = patients[c.patientId]
                    val patientName = "${patient?.nombre ?: ""} ${patient?.apellidos ?: ""}".lowercase()
                    patientName.contains(q) || c.reason.lowercase().contains(q) || c.diagnosis.lowercase().contains(q)
                }
            }
            return result
        }
}

sealed interface RecordsEvent {
    data class UpdateSearch(val query: String) : RecordsEvent
    data class UpdateFilter(val type: String) : RecordsEvent
}
