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
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.MonitorHeart
import androidx.compose.material.icons.outlined.MedicalServices
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
    navigateBack: () -> Unit,
    onEditProfile: (String) -> Unit = {},
    onEditClinical: (String) -> Unit = {},
    onEditAntecedents: (String) -> Unit = {},
    onNewConsultation: (String) -> Unit = {}
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    
    LaunchedEffect(id) {
        viewModel.onEvent(PatientEvent.LoadPatient(id))
    }
    
    PatientProfileBody(
        state = state,
        navigateBack = navigateBack,
        onEditProfile = { onEditProfile(id) },
        onEditClinical = { onEditClinical(id) },
        onEditAntecedents = { onEditAntecedents(id) },
        onNewConsultation = { onNewConsultation(id) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientProfileBody(
    state: PatientUiState,
    navigateBack: () -> Unit,
    onEditProfile: () -> Unit = {},
    onEditClinical: () -> Unit = {},
    onEditAntecedents: () -> Unit = {},
    onNewConsultation: () -> Unit = {}
) {
    val isDark = isSystemInDarkTheme()
    val bgColor = if (isDark) Color(0xFF121212) else Color(0xFFF8F9FA)
    val textColor = if (isDark) Color.White else Color(0xFF0A2342)
    val subtitleColor = if (isDark) Color.LightGray else Color(0xFF6B7280)
    val primaryPink = Color(0xFFEC5C76)
    val cardBg = if (isDark) Color(0xFF1E1E1E) else Color.White
    val borderColor = if (isDark) Color(0xFF333333) else Color(0xFFE0E0E0)
    val paleBg = if (isDark) Color(0xFF2A2A2A) else Color(0xFFF3F4F6)

    var selectedTab by remember { mutableIntStateOf(0) }
    var antecedentsExpanded by remember { mutableStateOf(false) }

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
        val obstetric = patient.obstetricHistory
        val antecedents = patient.antecedents

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // ID Badge
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

            // Patient Info Card
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
                            val ageText = if (patient.getAge() > 0) "${patient.getAge()} Años" else ""
                            val bloodText = if (patient.bloodType.isNotBlank()) " • ${patient.bloodType}" else ""
                            val genderText = if (patient.gender.isNotBlank()) " • ${patient.gender}" else ""
                            Text("$ageText$bloodText$genderText", color = subtitleColor, fontSize = 13.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            if (patient.ars.isNotBlank()) {
                                Box(
                                    modifier = Modifier
                                        .background(Color(0xFFE0F2FE), RoundedCornerShape(12.dp))
                                        .padding(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Text(patient.ars, color = Color(0xFF0284C7), fontSize = 11.sp, fontWeight = FontWeight.Medium)
                                }
                            }
                        }
                        
                        IconButton(onClick = onEditProfile) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit", tint = subtitleColor, modifier = Modifier.size(20.dp))
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Contact info
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
                    
                    // Antecedents collapsible
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { antecedentsExpanded = !antecedentsExpanded },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Info, contentDescription = "Info", tint = subtitleColor, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("ANTECEDENTES", color = textColor, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.weight(1f))
                        IconButton(onClick = onEditAntecedents, modifier = Modifier.size(24.dp)) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit", tint = subtitleColor, modifier = Modifier.size(18.dp))
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            if (antecedentsExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = "Expand",
                            tint = subtitleColor,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    if (antecedentsExpanded && antecedents != null) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            AntecedentRow("Familiares", antecedents.family, subtitleColor, textColor)
                            AntecedentRow("Patológicos", antecedents.pathological, subtitleColor, textColor)
                            AntecedentRow("Alergias", antecedents.allergies, subtitleColor, textColor)
                            AntecedentRow("Quirúrgicos", antecedents.surgical, subtitleColor, textColor)
                        }
                    } else if (antecedentsExpanded && antecedents == null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("No hay antecedentes registrados", color = subtitleColor, fontSize = 13.sp)
                    }
                }
            }

            // Gynecological Profile Card
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
                                .background(Color.White, CircleShape)
                                .clickable { onEditClinical() },
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
                                Text(patient.fum.ifBlank { "--" }, color = textColor, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text("CICLO", color = subtitleColor, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                Text(patient.ciclo.ifBlank { "--" }, color = textColor, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("MENARQUIA", color = subtitleColor, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                Text(
                                    patient.menarquia?.let { "${it} años" } ?: "--",
                                    color = textColor, fontSize = 14.sp, fontWeight = FontWeight.Medium
                                )
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text("PLANIFICACIÓN", color = subtitleColor, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                Text(patient.planificacion.ifBlank { "--" }, color = textColor, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                            }
                        }
                    }
                    
                    HorizontalDivider(color = borderColor, thickness = 1.dp)
                    
                    // Obstetric Stats (G, P, C, A)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        StatItem("G", obstetric?.g ?: 0)
                        StatItem("P", obstetric?.p ?: 0)
                        StatItem("C", obstetric?.c ?: 0)
                        StatItem("A", obstetric?.a ?: 0)
                    }
                }
            }

            // Tab Selector
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
                    TabIcon(Icons.Default.MonitorHeart, if (selectedTab == 0) primaryPink else subtitleColor, selectedTab == 0) { selectedTab = 0 }
                    TabIcon(Icons.Outlined.MedicalServices, if (selectedTab == 1) primaryPink else subtitleColor, selectedTab == 1) { selectedTab = 1 }
                    TabIcon(Icons.Default.ChildCare, if (selectedTab == 2) primaryPink else subtitleColor, selectedTab == 2) { selectedTab = 2 }
                }
            }

            // New Consultation Button
            Button(
                onClick = onNewConsultation,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF059669)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Nueva Consulta", tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Nueva Consulta", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }

            // Tab Content
            when (selectedTab) {
                0 -> ConsultationsTabContent(state, textColor, subtitleColor, primaryPink, cardBg, borderColor)
                1 -> PregnancyTabContent(patient, textColor, subtitleColor, primaryPink, cardBg, borderColor)
                2 -> SummaryTabContent(patient, textColor, subtitleColor, cardBg, borderColor)
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun ConsultationsTabContent(
    state: PatientUiState,
    textColor: Color,
    subtitleColor: Color,
    primaryPink: Color,
    cardBg: Color,
    borderColor: Color
) {
    Text(
        text = "Últimas Consultas",
        color = textColor,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(top = 8.dp)
    )

    if (state.patientAppointments.isEmpty()) {
        Text("No hay consultas registradas", color = subtitleColor, fontSize = 14.sp)
    } else {
        state.patientAppointments.sortedByDescending { it.startTime }.forEach { appointment ->
            Row(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
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
                                Text(appointment.type.uppercase(), color = primaryPink, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                            
                            Text(appointment.startTime, color = subtitleColor, fontSize = 12.sp)
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Text(appointment.notes ?: "Sin notas", color = textColor, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun PregnancyTabContent(
    patient: ucne.edu.ginecosys.domain.model.Patient,
    textColor: Color,
    subtitleColor: Color,
    primaryPink: Color,
    cardBg: Color,
    borderColor: Color
) {
    Text("Embarazos", color = textColor, fontSize = 16.sp, fontWeight = FontWeight.Bold)

    val activePregnancy = patient.activePregnancy
    if (activePregnancy != null) {
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = cardBg),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF059669).copy(alpha = 0.3f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .background(Color(0xFF059669).copy(alpha = 0.1f), RoundedCornerShape(4.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text("EMBARAZO ACTIVO", color = Color(0xFF059669), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Semanas", color = subtitleColor, fontSize = 11.sp)
                        Text("${activePregnancy.weeks}", color = textColor, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text("FPP", color = subtitleColor, fontSize = 11.sp)
                        Text(activePregnancy.fpp.ifBlank { "--" }, color = textColor, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Riesgo", color = subtitleColor, fontSize = 11.sp)
                        val riskColor = when (activePregnancy.risk) {
                            "Alto" -> Color(0xFFDC2626)
                            "Medio" -> Color(0xFFF59E0B)
                            else -> Color(0xFF059669)
                        }
                        Text(activePregnancy.risk, color = riskColor, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text("FCF", color = subtitleColor, fontSize = 11.sp)
                        Text(activePregnancy.lastFHR.ifBlank { "--" }, color = textColor, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                    }
                }
            }
        }
    } else {
        Text("No hay embarazo activo", color = subtitleColor, fontSize = 14.sp)
    }

    if (patient.pregnancies.isNotEmpty()) {
        Spacer(modifier = Modifier.height(8.dp))
        Text("Historial", color = textColor, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        patient.pregnancies.forEach { preg ->
            Card(
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = cardBg),
                border = androidx.compose.foundation.BorderStroke(1.dp, borderColor),
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
            ) {
                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text("${preg.year}", color = primaryPink, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(preg.outcome, color = textColor, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                        if (preg.details.isNotBlank()) {
                            Text(preg.details, color = subtitleColor, fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SummaryTabContent(
    patient: ucne.edu.ginecosys.domain.model.Patient,
    textColor: Color,
    subtitleColor: Color,
    cardBg: Color,
    borderColor: Color
) {
    Text("Resumen del Paciente", color = textColor, fontSize = 16.sp, fontWeight = FontWeight.Bold)
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        border = androidx.compose.foundation.BorderStroke(1.dp, borderColor),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            SummaryRow("Dirección", patient.direccion.ifBlank { "--" }, subtitleColor, textColor)
            SummaryRow("Ciudad", patient.ciudad.ifBlank { "--" }, subtitleColor, textColor)
            SummaryRow("Estado Civil", patient.civilStatus.ifBlank { "--" }, subtitleColor, textColor)
            SummaryRow("Tipo de Sangre", patient.bloodType.ifBlank { "--" }, subtitleColor, textColor)
            SummaryRow("ARS/Seguro", patient.ars.ifBlank { "--" }, subtitleColor, textColor)
        }
    }
}

@Composable
private fun SummaryRow(label: String, value: String, subtitleColor: Color, textColor: Color) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text("$label: ", color = subtitleColor, fontSize = 13.sp)
        Text(value, color = textColor, fontSize = 13.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun AntecedentRow(label: String, value: String, subtitleColor: Color, textColor: Color) {
    Column {
        Text(label.uppercase(), color = subtitleColor, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        Text(value.ifBlank { "No registrado" }, color = textColor, fontSize = 13.sp)
    }
}

@Composable
fun StatItem(label: String, value: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, color = Color(0xFFEC5C76), fontSize = 12.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(2.dp))
        Text(value.toString(), color = Color(0xFF991B1B), fontSize = 18.sp, fontWeight = FontWeight.Black)
    }
}

@Composable
fun RowScope.TabIcon(icon: androidx.compose.ui.graphics.vector.ImageVector, color: Color, selected: Boolean, onClick: () -> Unit = {}) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
            .height(56.dp)
            .clickable { onClick() },
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
