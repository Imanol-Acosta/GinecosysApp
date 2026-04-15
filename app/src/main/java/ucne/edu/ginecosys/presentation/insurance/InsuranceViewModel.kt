package ucne.edu.ginecosys.presentation.insurance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ucne.edu.ginecosys.domain.model.Insurance
import ucne.edu.ginecosys.domain.repository.InsuranceRepository
import javax.inject.Inject

@HiltViewModel
class InsuranceViewModel @Inject constructor(
    private val repository: InsuranceRepository
) : ViewModel() {

    private val _state = MutableStateFlow(InsuranceUiState())
    val state: StateFlow<InsuranceUiState> = _state.asStateFlow()

    init {
        loadInsurances()
    }

    fun onEvent(event: InsuranceEvent) {
        when (event) {
            is InsuranceEvent.LoadInsurances -> loadInsurances()
            is InsuranceEvent.UpdateName -> _state.update { it.copy(name = event.value) }
            is InsuranceEvent.UpdateType -> _state.update { it.copy(type = event.value) }
            is InsuranceEvent.UpdatePhone -> _state.update { it.copy(phone = event.value) }
            is InsuranceEvent.ToggleActive -> toggleActive(event.insurance)
            is InsuranceEvent.DeleteInsurance -> deleteInsurance(event.id)
            is InsuranceEvent.SelectForEdit -> selectForEdit(event.insurance)
            is InsuranceEvent.SaveInsurance -> save()
            is InsuranceEvent.UserMessageShown -> _state.update { it.copy(userMessage = null) }
            is InsuranceEvent.ShowAddDialog -> _state.update {
                it.copy(showDialog = true, editingInsurance = null, name = "", type = "", phone = "")
            }
            is InsuranceEvent.HideDialog -> _state.update { it.copy(showDialog = false) }
        }
    }

    private fun loadInsurances() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            repository.observeAll().collect { insurances ->
                _state.update { it.copy(isLoading = false, insurances = insurances) }
            }
        }
    }

    private fun selectForEdit(insurance: Insurance?) {
        if (insurance != null) {
            _state.update {
                it.copy(
                    showDialog = true,
                    editingInsurance = insurance,
                    name = insurance.name,
                    type = insurance.type,
                    phone = insurance.phone
                )
            }
        }
    }

    private fun toggleActive(insurance: Insurance) {
        viewModelScope.launch {
            repository.update(insurance.copy(isActive = !insurance.isActive))
        }
    }

    private fun deleteInsurance(id: String) {
        viewModelScope.launch {
            repository.delete(id)
            _state.update { it.copy(userMessage = "Seguro eliminado") }
        }
    }

    private fun save() {
        val s = _state.value
        if (s.name.isBlank()) {
            _state.update { it.copy(userMessage = "El nombre es requerido") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isSaving = true) }
            try {
                if (s.editingInsurance != null) {
                    repository.update(
                        s.editingInsurance.copy(
                            name = s.name.trim(),
                            type = s.type.trim(),
                            phone = s.phone.trim()
                        )
                    )
                    _state.update { it.copy(userMessage = "Seguro actualizado") }
                } else {
                    repository.add(
                        Insurance(
                            name = s.name.trim(),
                            type = s.type.trim(),
                            phone = s.phone.trim()
                        )
                    )
                    _state.update { it.copy(userMessage = "Seguro creado") }
                }
                _state.update { it.copy(isSaving = false, showDialog = false) }
            } catch (e: Exception) {
                _state.update { it.copy(isSaving = false, userMessage = "Error: ${e.localizedMessage}") }
            }
        }
    }
}
