package ucne.edu.ginecosys.presentation.navigation

import kotlinx.serialization.Serializable

sealed class Screen {
    @Serializable
    data object Login : Screen()

    @Serializable
    data object Main : Screen()

    @Serializable
    data object Home : Screen()

    @Serializable
    data object Patients : Screen()
    
    @Serializable
    data object AddPatient : Screen()
    
    @Serializable
    data class PatientProfile(val id: String) : Screen()

    @Serializable
    data object Appointments : Screen()

    @Serializable
    data object AddAppointment : Screen()

    @Serializable
    data class Detail(val id: Int) : Screen()
}
