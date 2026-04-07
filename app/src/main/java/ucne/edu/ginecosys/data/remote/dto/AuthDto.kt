package ucne.edu.ginecosys.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginRequestDto(
    val email: String,
    val password: String
)

@JsonClass(generateAdapter = true)
data class AuthResponseDto(
    @Json(name = "access_token") val accessToken: String,
    @Json(name = "refresh_token") val refreshToken: String,
    @Json(name = "user") val user: UserDto,
    @Json(name = "expires_in") val expiresIn: Int
)

@JsonClass(generateAdapter = true)
data class UserDto(
    val id: String,
    val email: String?
)
