package ucne.edu.ginecosys.data.repository

import android.util.Log
import ucne.edu.ginecosys.data.remote.SupabaseClinicApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ClinicRepository @Inject constructor(
    private val clinicApi: SupabaseClinicApi
) {
    private var _cachedClinicId: String? = null

    suspend fun getClinicId(): String? {
        if (_cachedClinicId != null) return _cachedClinicId
        return try {
            val clinics = clinicApi.getClinics()
            if (clinics.isNotEmpty()) {
                _cachedClinicId = clinics.first().id
                _cachedClinicId
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
