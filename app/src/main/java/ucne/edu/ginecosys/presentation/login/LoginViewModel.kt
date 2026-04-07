package ucne.edu.ginecosys.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ucne.edu.ginecosys.domain.repository.AuthRepository
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    init {
        // Automatically succeed if we already have a session
        if (authRepository.isUserLoggedIn()) {
            _uiState.update { it.copy(isSuccess = true) }
        }
    }

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.OnEmailChanged -> {
                _uiState.update { it.copy(email = event.email, error = null) }
            }
            is LoginEvent.OnPasswordChanged -> {
                _uiState.update { it.copy(password = event.password, error = null) }
            }
            is LoginEvent.Submit -> {
                login()
            }
        }
    }

    private fun login() {
        val email = _uiState.value.email
        val password = _uiState.value.password

        if (email.isBlank() || password.isBlank()) {
            _uiState.update { it.copy(error = "El correo y contraseña son requeridos.") }
            return
        }

        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            val result = authRepository.login(email, password)
            if (result.isSuccess) {
                _uiState.update { it.copy(isLoading = false, isSuccess = true) }
            } else {
                _uiState.update { 
                    it.copy(
                        isLoading = false, 
                        error = "Credenciales incorrectas o problema de red."
                    ) 
                }
            }
        }
    }
}
