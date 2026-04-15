package ucne.edu.ginecosys.data.remote

import retrofit2.http.*
import ucne.edu.ginecosys.data.remote.dto.ConsultationDto

interface SupabaseConsultationApi {
    @GET("rest/v1/consultations")
    suspend fun getConsultations(
        @Query("select") select: String = "*"
        // Removed clinic_id filter as it doesn't exist in the remote schema
    ): List<ConsultationDto>

    @POST("rest/v1/consultations")
    suspend fun createConsultation(@Body dto: ConsultationDto): List<ConsultationDto>

    @PATCH("rest/v1/consultations")
    suspend fun updateConsultation(
        @Query("id") eqId: String,
        @Body dto: ConsultationDto
    ): List<ConsultationDto>

    @DELETE("rest/v1/consultations")
    suspend fun deleteConsultation(@Query("id") eqId: String)
}
