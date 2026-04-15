package ucne.edu.ginecosys.presentation.navigation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ucne.edu.ginecosys.domain.repository.AuthRepository
import javax.inject.Inject

@HiltViewModel
class LogoutViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    suspend fun logout() {
        authRepository.logout()
    }
}
