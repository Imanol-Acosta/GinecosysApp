package ucne.edu.ginecosys.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ucne.edu.ginecosys.presentation.home.HomeScreen
import ucne.edu.ginecosys.presentation.detail.DetailScreen

@Composable
fun NavHost(
    navHostController: NavHostController
) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.Home
    ) {
        composable<Screen.Home> {
            HomeScreen(
                goToDetail = {
                    navHostController.navigate(Screen.Detail(id = 0))
                }
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
