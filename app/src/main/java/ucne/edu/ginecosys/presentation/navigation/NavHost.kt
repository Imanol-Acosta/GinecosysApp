package ucne.edu.ginecosys.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import ucne.edu.ginecosys.presentation.detail.DetailScreen
import ucne.edu.ginecosys.presentation.login.LoginScreen

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
            MainScreen(rootNavController = navHostController)
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
    }
}
