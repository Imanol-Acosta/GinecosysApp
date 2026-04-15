package ucne.edu.ginecosys.data.remote

import retrofit2.http.*
import ucne.edu.ginecosys.data.remote.dto.InsuranceDto

interface SupabaseInsuranceApi {
    @GET("rest/v1/insurances")
    suspend fun getInsurances(): List<InsuranceDto>

    @POST("rest/v1/insurances")
    suspend fun createInsurance(@Body dto: InsuranceDto): List<InsuranceDto>

    @PATCH("rest/v1/insurances")
    suspend fun updateInsurance(@Query("id") eqId: String, @Body dto: InsuranceDto): List<InsuranceDto>

    @DELETE("rest/v1/insurances")
    suspend fun deleteInsurance(@Query("id") eqId: String)
}
