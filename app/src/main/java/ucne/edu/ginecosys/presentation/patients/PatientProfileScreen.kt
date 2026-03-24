package ucne.edu.ginecosys.presentation.patients

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChildCare
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.MonitorHeart
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.MedicalServices
import androidx.compose.material.icons.outlined.Medication
import androidx.compose.material.icons.outlined.Science
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ucne.edu.ginecosys.domain.model.getAge
import ucne.edu.ginecosys.domain.model.getInitials

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientProfileScreen(
    id: String,
    viewModel: PatientViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    
    LaunchedEffect(id) {
        viewModel.onEvent(PatientEvent.LoadPatient(id))
    }
    
    PatientProfileBody(
        state = state,
        navigateBack = navigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientProfileBody(
    state: PatientUiState,
    navigateBack: () -> Unit
) {
    val isDark = isSystemInDarkTheme()
    val bgColor = if (isDark) Color(0xFF121212) else Color(0xFFF8F9FA)
    val textColor = if (isDark) Color.White else Color(0xFF0A2342)
    val subtitleColor = if (isDark) Color.LightGray else Color(0xFF6B7280)
    val primaryPink = Color(0xFFEC5C76)
    val cardBg = if (isDark) Color(0xFF1E1E1E) else Color.White
    val borderColor = if (isDark) Color(0xFF333333) else Color(0xFFE0E0E0)
    val paleBg = if (isDark) Color(0xFF2A2A2A) else Color(0xFFF3F4F6)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Volver al Directorio", fontSize = 16.sp, color = subtitleColor)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = subtitleColor)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = bgColor)
            )
        },
        containerColor = bgColor
    ) { paddingValues ->
        val patient = state.currentPatient
        
        if (patient == null) {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = primaryPink)
            }
            return@Scaffold
        }

        val fullName = "${patient.nombre} ${patient.apellidos}"

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .background(paleBg, RoundedCornerShape(8.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "ID: ${patient.id.take(8).uppercase()}",
                    color = subtitleColor,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = cardBg),
                border = androidx.compose.foundation.BorderStroke(1.dp, borderColor),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .background(primaryPink, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(patient.getInitials(), color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        }
                        
                        Spacer(modifier = Modifier.width(16.dp))
                        
                        Column(modifier = Modifier.weight(1f)) {
                            Text(fullName, color = textColor, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text("${patient.getAge()} Años • ${patient.bloodType} • ${patient.gender}", color = subtitleColor, fontSize = 13.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Box(
                                modifier = Modifier
                                    .background(Color(0xFFE0F2FE), RoundedCornerShape(12.dp))
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(patient.insuranceType, color = Color(0xFF0284C7), fontSize = 11.sp, fontWeight = FontWeight.Medium)
                            }
                        }
                        
                        Icon(Icons.Default.Edit, contentDescription = "Edit", tint = subtitleColor, modifier = Modifier.size(20.dp))
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(paleBg, RoundedCornerShape(8.dp))
                            .padding(12.dp)
                    ) {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            val ced = if (patient.cedula.isNotBlank()) patient.cedula else "--"
                            val tel = if (patient.telefono.isNotBlank()) patient.telefono else "--"
                            Text("Cédula: ", color = subtitleColor, fontSize = 13.sp)
                            Text(ced, color = textColor, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                            
                            Spacer(modifier = Modifier.width(16.dp))
                            
                            Text("Tel: ", color = subtitleColor, fontSize = 13.sp)
                            Text(tel, color = textColor, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = borderColor, thickness = 1.dp)
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Info, contentDescription = "Info", tint = subtitleColor, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("ANTECEDENTES", color = textColor, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.weight(1f))
                        Icon(Icons.Default.Edit, contentDescription = "Edit", tint = subtitleColor, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Expand", tint = subtitleColor, modifier = Modifier.size(20.dp))
                    }
                }
            }

            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = cardBg),
                border = androidx.compose.foundation.BorderStroke(1.dp, primaryPink.copy(alpha = 0.2f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(primaryPink.copy(alpha = 0.05f))
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.MonitorHeart, contentDescription = "Pulse", tint = primaryPink, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Perfil Ginecológico", color = primaryPink, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.weight(1f))
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .border(1.dp, primaryPink.copy(alpha = 0.5f), CircleShape)
                                .background(Color.White, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit", tint = primaryPink, modifier = Modifier.size(16.dp))
                        }
                    }
                    
                    HorizontalDivider(color = primaryPink.copy(alpha = 0.1f), thickness = 1.dp)
                    
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("F.U.M.", color = subtitleColor, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                Text(patient.fum, color = textColor, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text("CICLO", color = subtitleColor, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                Text(patient.ciclo, color = textColor, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("MENARQUIA", color = subtitleColor, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                Text(patient.menarquia, color = textColor, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text("PLANIFICACIÓN", color = subtitleColor, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                Text(patient.planificacion, color = textColor, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                            }
                        }
                    }
                    
                    HorizontalDivider(color = borderColor, thickness = 1.dp)
                    
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        StatItem("G", patient.gestaciones)
                        StatItem("P", patient.partos)
                        StatItem("C", patient.cesareas)
                        StatItem("A", patient.abortos)
                    }
                }
            }

            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = cardBg),
                border = androidx.compose.foundation.BorderStroke(1.dp, borderColor),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TabIcon(Icons.Default.MonitorHeart, primaryPink, true)
                    TabIcon(Icons.Outlined.MedicalServices, subtitleColor, false)
                    TabIcon(Icons.Default.ChildCare, subtitleColor, false)
                    TabIcon(Icons.Outlined.Description, subtitleColor, false)
                }
            }

            Button(
                onClick = { /* TODO */ },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF059669)), // Green
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Nueva Consulta", tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Nueva Consulta", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ActionButton(
                    icon = { Icon(Icons.Outlined.Medication, contentDescription = null, tint = primaryPink, modifier = Modifier.size(20.dp)) },
                    text = "Receta",
                    modifier = Modifier.weight(1f)
                )
                ActionButton(
                    icon = { Icon(Icons.Outlined.Description, contentDescription = null, tint = Color(0xFFF59E0B), modifier = Modifier.size(20.dp)) },
                    text = "Licencia",
                    modifier = Modifier.weight(1f)
                )
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ActionButton(
                    icon = { Icon(Icons.Outlined.Description, contentDescription = null, tint = Color(0xFF3B82F6), modifier = Modifier.size(20.dp)) },
                    text = "Indicación",
                    modifier = Modifier.weight(1f)
                )
                ActionButton(
                    icon = { Icon(Icons.Outlined.Science, contentDescription = null, tint = Color(0xFF0EA5E9), modifier = Modifier.size(20.dp)) },
                    text = "Estudio",
                    modifier = Modifier.weight(1f)
                )
            }

            Text(
                text = "Últimas Consultas",
                color = textColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp)
            )

            Row(modifier = Modifier.fillMaxWidth()) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(top = 8.dp, end = 12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .border(2.dp, primaryPink, CircleShape)
                            .background(Color.Transparent, CircleShape)
                    )
                    Box(
                        modifier = Modifier
                            .width(2.dp)
                            .height(100.dp)
                            .background(primaryPink.copy(alpha = 0.3f))
                    )
                }

                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = cardBg),
                    border = androidx.compose.foundation.BorderStroke(1.dp, borderColor),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(
                                modifier = Modifier
                                    .background(primaryPink.copy(alpha = 0.1f), RoundedCornerShape(4.dp))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text("CONSULTA GENERAL", color = primaryPink, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                            
                            Text("20 de marzo de 2026", color = subtitleColor, fontSize = 12.sp)
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Text("xx", color = textColor, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun StatItem(label: String, value: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, color = Color(0xFFEC5C76), fontSize = 12.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(2.dp))
        Text(value.toString(), color = Color(0xFF991B1B), fontSize = 18.sp, fontWeight = FontWeight.Black) // Dark red color
    }
}

@Composable
fun RowScope.TabIcon(icon: androidx.compose.ui.graphics.vector.ImageVector, color: Color, selected: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
            .height(56.dp)
            .clickable { },
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(24.dp))
            if (selected) {
                Spacer(modifier = Modifier.height(4.dp))
                Box(modifier = Modifier.width(32.dp).height(2.dp).background(color))
            }
        }
    }
}

@Composable
fun RowScope.ActionButton(
    icon: @Composable () -> Unit,
    text: String,
    modifier: Modifier = Modifier
) {
    val isDark = isSystemInDarkTheme()
    val borderColor = if (isDark) Color(0xFF333333) else Color(0xFFE0E0E0)
    
    OutlinedButton(
        onClick = { /* TODO */ },
        shape = RoundedCornerShape(8.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, borderColor),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if (isDark) Color(0xFF1E1E1E) else Color.White
        ),
        modifier = modifier.height(48.dp)
    ) {
        icon()
        Spacer(modifier = Modifier.width(8.dp))
        Text(text, color = if (isDark) Color.White else Color(0xFF0A2342), fontSize = 13.sp, fontWeight = FontWeight.Medium)
    }
}
