package ucne.edu.ginecosys.data.remote

import com.squareup.moshi.JsonClass
import retrofit2.http.GET
import retrofit2.http.Query

@JsonClass(generateAdapter = true)
data class ClinicDto(val id: String)

interface SupabaseClinicApi {
    @GET("rest/v1/clinics")
    suspend fun getClinics(
        @Query("select") select: String = "id",
        @Query("limit") limit: Int = 1
    ): List<ClinicDto>
}
