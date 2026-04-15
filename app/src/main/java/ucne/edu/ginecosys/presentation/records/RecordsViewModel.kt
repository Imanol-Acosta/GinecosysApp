package ucne.edu.ginecosys.presentation.records

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ucne.edu.ginecosys.domain.usecase.ObserveAllConsultationsUseCase
import ucne.edu.ginecosys.domain.usecase.ObservePatientsUseCase
import javax.inject.Inject

@HiltViewModel
class RecordsViewModel @Inject constructor(
    private val observeAllConsultationsUseCase: ObserveAllConsultationsUseCase,
    private val observePatientsUseCase: ObservePatientsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(RecordsUiState())
    val state: StateFlow<RecordsUiState> = _state.asStateFlow()

    init {
        loadData()
    }

    fun onEvent(event: RecordsEvent) {
        when (event) {
            is RecordsEvent.UpdateSearch -> _state.update { it.copy(searchQuery = event.query) }
            is RecordsEvent.UpdateFilter -> _state.update { it.copy(filterType = event.type) }
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            // Load consultations
            launch {
                observeAllConsultationsUseCase().collect { consultations ->
                    _state.update { it.copy(consultations = consultations, isLoading = false) }
                }
            }
            // Load patients for name lookup
            launch {
                observePatientsUseCase().collect { patients ->
                    _state.update { it.copy(patients = patients.associateBy { p -> p.id }) }
                }
            }
        }
    }
}
