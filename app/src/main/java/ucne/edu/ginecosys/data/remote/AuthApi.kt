package ucne.edu.ginecosys.data.remote

import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import ucne.edu.ginecosys.data.remote.dto.AuthResponseDto
import ucne.edu.ginecosys.data.remote.dto.LoginRequestDto

interface AuthApi {
    @POST("auth/v1/token")
    suspend fun login(
        @Query("grant_type") grantType: String = "password",
        @Body request: LoginRequestDto
    ): AuthResponseDto
}
