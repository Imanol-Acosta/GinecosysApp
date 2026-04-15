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

    // Phase 1 - Edit Patient
    @Serializable
    data class EditPatient(val id: String) : Screen()

    @Serializable
    data class EditClinicalData(val id: String) : Screen()

    @Serializable
    data class EditAntecedents(val id: String) : Screen()

    // Phase 2 - Consultations
    @Serializable
    data class NewConsultation(val patientId: String) : Screen()

    @Serializable
    data class EditConsultation(val consultationId: String) : Screen()

    // Phase 4 - Records
    @Serializable
    data object Records : Screen()

    // Phase 5 - Insurance
    @Serializable
    data object Insurance : Screen()

    // Phase 6 - Checkout
    @Serializable
    data class Checkout(val appointmentId: String) : Screen()
}
