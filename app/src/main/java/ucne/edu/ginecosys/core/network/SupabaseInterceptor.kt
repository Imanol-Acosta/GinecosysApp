package ucne.edu.ginecosys.core.network

import okhttp3.Interceptor
import okhttp3.Response
import ucne.edu.ginecosys.core.config.SupabaseConfig
import ucne.edu.ginecosys.data.local.preferences.SessionManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SupabaseInterceptor @Inject constructor(
    private val sessionManager: SessionManager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val requestBuilder = originalRequest.newBuilder()
            .header("apikey", SupabaseConfig.ANON_KEY)
            .header("Prefer", "return=representation") // Useful for POST/PATCH to return the affected object

        val accessToken = sessionManager.getAccessToken()
        if (!accessToken.isNullOrEmpty()) {
            requestBuilder.header("Authorization", "Bearer $accessToken")
        } else {
            requestBuilder.header("Authorization", "Bearer ${SupabaseConfig.ANON_KEY}")
        }

        return chain.proceed(requestBuilder.build())
    }
}
