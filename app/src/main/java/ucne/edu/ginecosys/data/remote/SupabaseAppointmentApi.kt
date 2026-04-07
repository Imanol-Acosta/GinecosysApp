package ucne.edu.ginecosys.data.remote

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import ucne.edu.ginecosys.data.remote.dto.AppointmentDto

interface SupabaseAppointmentApi {
    @GET("rest/v1/appointments")
    suspend fun getAppointments(
        @Query("select") select: String = "*"
    ): List<AppointmentDto>

    @POST("rest/v1/appointments")
    suspend fun insertAppointment(
        @Body appointment: AppointmentDto
    )
}
