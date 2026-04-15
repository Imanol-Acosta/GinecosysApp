package ucne.edu.ginecosys.presentation.patients

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FilterAlt
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ucne.edu.ginecosys.domain.model.getAge
import ucne.edu.ginecosys.domain.model.getInitials

val AVATAR_COLORS = listOf(
    Color(0xFFEC5C76), Color(0xFF3B82F6), Color(0xFF10B981), Color(0xFF8B5CF6), Color(0xFFF59E0B)
)

fun getAvatarColor(nome: String): Color {
    return AVATAR_COLORS[Math.abs(nome.hashCode()) % AVATAR_COLORS.size]
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientsScreen(
    viewModel: PatientViewModel = hiltViewModel(),
    goToAddPatient: () -> Unit,
    goToPatientProfile: (String) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    
    PatientsBody(
        state = state,
        onEvent = viewModel::onEvent,
        goToAddPatient = goToAddPatient,
        goToPatientProfile = goToPatientProfile
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientsBody(
    state: PatientUiState,
    onEvent: (PatientEvent) -> Unit,
    goToAddPatient: () -> Unit,
    goToPatientProfile: (String) -> Unit
) {
    val isDark = isSystemInDarkTheme()
    val bgColor = if (isDark) Color(0xFF121212) else Color(0xFFF8F9FA)
    val textColor = if (isDark) Color.White else Color(0xFF0A2342)
    val primaryPink = Color(0xFFEC5C76)
    val subtitleColor = if (isDark) Color.LightGray else Color.Gray
    val cardBg = if (isDark) Color(0xFF1E1E1E) else Color.White
    val borderColor = if (isDark) Color(0xFF333333) else Color(0xFFE0E0E0)
    
    Scaffold(
        containerColor = bgColor
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Directorio de Pacientes",
                    color = textColor,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Gestión centralizada de expedientes y demografía.",
                    color = subtitleColor,
                    fontSize = 14.sp
                )
                
                Spacer(modifier = Modifier.height(20.dp))
                
                Button(
                    onClick = goToAddPatient,
                    modifier = Modifier.height(48.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = primaryPink)
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Nuevo Paciente", tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Nuevo Paciente", color = Color.White, fontWeight = FontWeight.SemiBold)
                }
                
                Spacer(modifier = Modifier.height(24.dp))
            }
            
            Card(
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomStart = 0.dp, bottomEnd = 0.dp),
                colors = CardDefaults.cardColors(containerColor = cardBg),
                border = androidx.compose.foundation.BorderStroke(1.dp, borderColor),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        OutlinedTextField(
                            value = state.searchQuery,
                            onValueChange = { onEvent(PatientEvent.SearchPatients(it)) },
                            placeholder = { Text("Buscar por Nombre, Cédula o ARS...", color = Color.Gray) },
                            leadingIcon = {
                                Icon(Icons.Filled.Search, contentDescription = "Search", tint = Color.Gray)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = primaryPink,
                                unfocusedBorderColor = borderColor,
                                focusedContainerColor = if (isDark) Color(0xFF2A2A2A) else Color.White,
                                unfocusedContainerColor = if (isDark) Color(0xFF2A2A2A) else Color.White
                            )
                        )
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        OutlinedButton(
                            onClick = { /* TODO a filter modal */ },
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = textColor),
                            border = androidx.compose.foundation.BorderStroke(1.dp, borderColor),
                            modifier = Modifier.height(40.dp)
                        ) {
                            Icon(Icons.Outlined.FilterAlt, contentDescription = "Filtros", modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Filtros", fontWeight = FontWeight.Medium)
                        }
                    }
                    
                    HorizontalDivider(color = borderColor, thickness = 1.dp)
                    
                    if (state.patients.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Box(
                                    modifier = Modifier
                                        .size(72.dp)
                                        .background(if (isDark) Color(0xFF333333) else Color(0xFFF3F4F6), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(36.dp), tint = Color.Gray)
                                }
                                Spacer(modifier = Modifier.height(16.dp))
                                Text("No hay pacientes todavía", color = textColor, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                Text(
                                    "Haz clic en 'Nuevo Paciente' para agregar el primero a la lista.",
                                    color = subtitleColor, fontSize = 14.sp, textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(top = 8.dp, start = 32.dp, end = 32.dp)
                                )
                            }
                        }
                    } else if (state.filteredPatients.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("No se encontraron resultados para la búsqueda.", color = subtitleColor)
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(state.filteredPatients) { patient ->
                                val fullName = "${patient.nombre} ${patient.apellidos}"
                                PatientListItem(
                                    initials = patient.getInitials(),
                                    avatarColor = getAvatarColor(fullName),
                                    name = fullName,
                                    type = "General",
                                    idOrCedula = if (patient.cedula.isNotBlank()) patient.cedula else "Sin Cédula",
                                    ageAndGender = "${patient.getAge()} Años - ${patient.gender}",
                                    insuranceType = patient.ars.ifBlank { "Sin ARS" },
                                    textColor = textColor,
                                    subtitleColor = subtitleColor,
                                    borderColor = borderColor,
                                    onClick = { goToPatientProfile(patient.id) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PatientListItem(
    initials: String,
    avatarColor: Color,
    name: String,
    type: String, 
    idOrCedula: String,
    ageAndGender: String, 
    insuranceType: String,
    textColor: Color,
    subtitleColor: Color,
    borderColor: Color,
    onClick: () -> Unit
) {
    Column(modifier = Modifier.clickable { onClick() }) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(avatarColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(initials, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(name, color = textColor, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        Icons.AutoMirrored.Filled.KeyboardArrowRight, 
                        contentDescription = "Ver Detalle", 
                        tint = Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                }
                
                Text(type, color = Color(0xFF10B981), fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                
                Row(modifier = Modifier.padding(top = 4.dp)) {
                    Text(idOrCedula, color = subtitleColor, fontSize = 11.sp)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(ageAndGender, color = subtitleColor, fontSize = 11.sp)
                }
                
                Text(insuranceType, color = textColor, fontSize = 11.sp, modifier = Modifier.padding(top = 4.dp))
            }
        }
        HorizontalDivider(color = borderColor, thickness = 1.dp)
    }
}
