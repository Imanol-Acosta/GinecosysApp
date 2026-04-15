package ucne.edu.ginecosys.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ucne.edu.ginecosys.presentation.appointments.AppointmentsScreen
import ucne.edu.ginecosys.presentation.home.HomeScreen
import ucne.edu.ginecosys.presentation.insurance.InsuranceScreen
import ucne.edu.ginecosys.presentation.patients.PatientsScreen
import ucne.edu.ginecosys.presentation.records.RecordsScreen

data class BottomNavItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val route: Screen
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    rootNavController: NavHostController,
    onLogout: () -> Unit = {},
    navController: NavHostController = rememberNavController()
) {
    var showLogoutDialog by remember { mutableStateOf(false) }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            icon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Logout,
                    contentDescription = null,
                    tint = Color(0xFFEC5C76)
                )
            },
            title = {
                Text(
                    text = "Cerrar Sesión",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text("¿Estás seguro de que deseas cerrar sesión?")
            },
            confirmButton = {
                Button(
                    onClick = {
                        showLogoutDialog = false
                        onLogout()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFEC5C76)
                    )
                ) {
                    Text("Cerrar Sesión")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    val items = listOf(
        BottomNavItem(
            title = "Inicio",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
            route = Screen.Home
        ),
        BottomNavItem(
            title = "Agenda",
            selectedIcon = Icons.Filled.Event,
            unselectedIcon = Icons.Outlined.Event,
            route = Screen.Appointments
        ),
        BottomNavItem(
            title = "Pacientes",
            selectedIcon = Icons.Filled.People,
            unselectedIcon = Icons.Outlined.People,
            route = Screen.Patients
        ),
        BottomNavItem(
            title = "Récords",
            selectedIcon = Icons.Filled.Description,
            unselectedIcon = Icons.Outlined.Description,
            route = Screen.Records
        ),
        BottomNavItem(
            title = "Seguros",
            selectedIcon = Icons.Filled.Shield,
            unselectedIcon = Icons.Outlined.Shield,
            route = Screen.Insurance
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "GinecoSys",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFEC5C76)
                    )
                },
                actions = {
                    IconButton(onClick = { showLogoutDialog = true }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Logout,
                            contentDescription = "Cerrar Sesión",
                            tint = Color.Gray
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                items.forEach { item ->
                    val isSelected = currentDestination?.hierarchy?.any { 
                        it.route?.contains(item.route::class.simpleName ?: "") == true 
                    } == true
                    
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                                contentDescription = item.title,
                                tint = if (isSelected) Color(0xFFEC5C76) else Color.Gray
                            )
                        },
                        label = {
                            Text(
                                text = item.title,
                                color = if (isSelected) Color(0xFFEC5C76) else Color.Gray,
                                fontSize = 10.sp,
                                maxLines = 1
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = Color(0xFFEC5C76).copy(alpha = 0.1f)
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable<Screen.Home> {
                HomeScreen(
                    goToDetail = {
                        rootNavController.navigate(Screen.Detail(id = 0))
                    },
                    goToAddAppointment = {
                        rootNavController.navigate(Screen.AddAppointment)
                    },
                    goToAddPatient = {
                        rootNavController.navigate(Screen.AddPatient)
                    }
                )
            }
            
            composable<Screen.Appointments> {
                AppointmentsScreen(
                    goToAddAppointment = {
                        rootNavController.navigate(Screen.AddAppointment)
                    },
                    goToDetail = {
                        rootNavController.navigate(Screen.Detail(id = 0))
                    }
                )
            }
            
            composable<Screen.Patients> {
                PatientsScreen(
                    goToAddPatient = {
                        rootNavController.navigate(Screen.AddPatient)
                    },
                    goToPatientProfile = { patientId ->
                        rootNavController.navigate(Screen.PatientProfile(id = patientId))
                    }
                )
            }

            composable<Screen.Records> {
                RecordsScreen()
            }

            composable<Screen.Insurance> {
                InsuranceScreen()
            }
        }
    }
}
