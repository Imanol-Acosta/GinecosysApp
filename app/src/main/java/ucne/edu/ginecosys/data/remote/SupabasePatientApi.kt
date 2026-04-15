package ucne.edu.ginecosys.data.remote

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query
import ucne.edu.ginecosys.data.remote.dto.PatientDto

interface SupabasePatientApi {
    @GET("rest/v1/patients")
    suspend fun getPatients(
        @Query("select") select: String = "*",
        @Query("clinic_id") eqClinicId: String
    ): List<PatientDto>


    @POST("rest/v1/patients")
    suspend fun createPatient(@Body patient: PatientDto): List<PatientDto>
    
    // Usage: updatePatient("eq.${patient.id}", patient)
    @PATCH("rest/v1/patients")
    suspend fun updatePatient(
        @Query("id") eqId: String,
        @Body patient: PatientDto
    ): List<PatientDto>

    // Partial update with Map body for specific field updates
    @PATCH("rest/v1/patients")
    suspend fun updatePatientFields(
        @Query("id") eqId: String,
        @Body fields: Map<String, @JvmSuppressWildcards Any?>
    )

    // Usage: deletePatient("eq.${id}")
    @DELETE("rest/v1/patients")
    suspend fun deletePatient(@Query("id") eqId: String)
}
