package ucne.edu.ginecosys.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlinx.coroutines.launch
import ucne.edu.ginecosys.presentation.detail.DetailScreen
import ucne.edu.ginecosys.presentation.login.LoginScreen
import ucne.edu.ginecosys.presentation.patients.edit.EditAntecedentsScreen
import ucne.edu.ginecosys.presentation.patients.edit.EditClinicalDataScreen
import ucne.edu.ginecosys.presentation.patients.edit.EditPatientScreen

@Composable
fun NavHost(
    navHostController: NavHostController
) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.Login
    ) {
        composable<Screen.Login> {
            LoginScreen(
                onLoginSuccess = {
                    navHostController.navigate(Screen.Main) {
                        popUpTo(Screen.Login) { inclusive = true }
                    }
                }
            )
        }

        composable<Screen.Main> {
            val logoutViewModel: LogoutViewModel = hiltViewModel()
            val scope = rememberCoroutineScope()

            MainScreen(
                rootNavController = navHostController,
                onLogout = {
                    scope.launch {
                        logoutViewModel.logout()
                        navHostController.navigate(Screen.Login) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                }
            )
        }

        composable<Screen.AddPatient> {
            ucne.edu.ginecosys.presentation.patients.AddPatientScreen(
                navigateBack = { navHostController.navigateUp() }
            )
        }

        composable<Screen.PatientProfile> { backStackEntry ->
            val profileRoute = backStackEntry.toRoute<Screen.PatientProfile>()
            ucne.edu.ginecosys.presentation.patients.PatientProfileScreen(
                id = profileRoute.id,
                navigateBack = { navHostController.navigateUp() },
                onEditProfile = { id -> navHostController.navigate(Screen.EditPatient(id)) },
                onEditClinical = { id -> navHostController.navigate(Screen.EditClinicalData(id)) },
                onEditAntecedents = { id -> navHostController.navigate(Screen.EditAntecedents(id)) },
                onNewConsultation = { id -> navHostController.navigate(Screen.NewConsultation(id)) }
            )
        }

        composable<Screen.EditPatient> { backStackEntry ->
            val route = backStackEntry.toRoute<Screen.EditPatient>()
            EditPatientScreen(
                patientId = route.id,
                navigateBack = { navHostController.navigateUp() }
            )
        }

        composable<Screen.EditClinicalData> { backStackEntry ->
            val route = backStackEntry.toRoute<Screen.EditClinicalData>()
            EditClinicalDataScreen(
                patientId = route.id,
                navigateBack = { navHostController.navigateUp() }
            )
        }

        composable<Screen.EditAntecedents> { backStackEntry ->
            val route = backStackEntry.toRoute<Screen.EditAntecedents>()
            EditAntecedentsScreen(
                patientId = route.id,
                navigateBack = { navHostController.navigateUp() }
            )
        }

        composable<Screen.AddAppointment> {
            ucne.edu.ginecosys.presentation.appointments.AddAppointmentScreen(
                navigateBack = { navHostController.navigateUp() }
            )
        }

        composable<Screen.Detail> {
            DetailScreen(
                navigateBack = {
                    navHostController.navigateUp()
                }
            )
        }

        // Phase 2 - Consultations
        composable<Screen.NewConsultation> { backStackEntry ->
            val route = backStackEntry.toRoute<Screen.NewConsultation>()
            ucne.edu.ginecosys.presentation.consultations.NewConsultationScreen(
                patientId = route.patientId,
                navigateBack = { navHostController.navigateUp() }
            )
        }

        // Phase 6 - Checkout
        composable<Screen.Checkout> { backStackEntry ->
            val route = backStackEntry.toRoute<Screen.Checkout>()
            ucne.edu.ginecosys.presentation.checkout.CheckoutScreen(
                appointmentId = route.appointmentId,
                navigateBack = { navHostController.navigateUp() }
            )
        }
    }
}
