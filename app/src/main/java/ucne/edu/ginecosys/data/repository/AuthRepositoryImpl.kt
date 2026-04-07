package ucne.edu.ginecosys.data.repository

import ucne.edu.ginecosys.data.local.preferences.SessionManager
import ucne.edu.ginecosys.data.remote.AuthApi
import ucne.edu.ginecosys.data.remote.dto.LoginRequestDto
import ucne.edu.ginecosys.domain.repository.AuthRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val sessionManager: SessionManager
) : AuthRepository {
    override suspend fun login(email: String, password: String): Result<Unit> {
        return try {
            val response = authApi.login(request = LoginRequestDto(email, password))
            sessionManager.saveAuthTokens(response.accessToken, response.refreshToken)
            Result.success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun logout() {
        sessionManager.clearSession()
    }

    override fun isUserLoggedIn(): Boolean {
        return sessionManager.getAccessToken() != null
    }
}
