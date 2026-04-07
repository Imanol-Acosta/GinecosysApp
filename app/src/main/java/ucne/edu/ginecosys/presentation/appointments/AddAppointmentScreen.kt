package ucne.edu.ginecosys.presentation.appointments

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Description
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAppointmentScreen(
    viewModel: AppointmentViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    val isDark = isSystemInDarkTheme()
    val bgColor = if (isDark) Color(0xFF121212) else Color(0xFFF8F9FA)
    val textColor = if (isDark) Color.White else Color(0xFF0A2342)
    val subtitleColor = if (isDark) Color.LightGray else Color(0xFF6B7280)
    val primaryPink = Color(0xFFEC5C76)
    val cardBg = if (isDark) Color(0xFF1E1E1E) else Color.White
    val borderColor = if (isDark) Color(0xFF333333) else Color(0xFFE0E0E0)

    // Navigate back after save
    LaunchedEffect(state.appointmentSaved) {
        if (state.appointmentSaved) {
            viewModel.onEvent(AppointmentEvent.ResetForm)
            navigateBack()
        }
    }

    // Reset form when entering
    LaunchedEffect(Unit) {
        viewModel.onEvent(AppointmentEvent.ResetForm)
    }

    val appointmentTypes = listOf(
        "Consulta General", "Control Prenatal", "Ultrasonido",
        "Procedimiento", "Emergencia", "Seguimiento"
    )
    var showTypeDropdown by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    // Date Picker Dialog
    if (showDatePicker) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val cal = java.util.Calendar.getInstance()
                        cal.timeInMillis = millis
                        val formatted = String.format(
                            java.util.Locale.US, "%04d-%02d-%02d",
                            cal.get(java.util.Calendar.YEAR),
                            cal.get(java.util.Calendar.MONTH) + 1,
                            cal.get(java.util.Calendar.DAY_OF_MONTH)
                        )
                        viewModel.onEvent(AppointmentEvent.UpdateFormDate(formatted))
                    }
                    showDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancelar") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    // Time Picker Dialog
    if (showTimePicker) {
        val timePickerState = rememberTimePickerState(initialHour = 9, initialMinute = 0)
        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    val formatted = String.format(
                        java.util.Locale.US, "%02d:%02d",
                        timePickerState.hour, timePickerState.minute
                    )
                    viewModel.onEvent(AppointmentEvent.UpdateFormTime(formatted))
                    showTimePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) { Text("Cancelar") }
            },
            text = { TimePicker(state = timePickerState) }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Nueva Cita", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = textColor)
                        Text("Seleccione un paciente y agende su cita.", fontSize = 12.sp, color = subtitleColor)
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Patient Search
            item {
                Spacer(modifier = Modifier.height(4.dp))
                Text("Paciente", color = textColor, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(8.dp))

                // Selected patient display
                if (state.selectedPatientId != null) {
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = primaryPink.copy(alpha = 0.08f)),
                        border = androidx.compose.foundation.BorderStroke(1.dp, primaryPink.copy(alpha = 0.3f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier.size(40.dp).background(primaryPink, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                val initials = state.selectedPatientName.split(" ")
                                    .take(2).joinToString("") { it.take(1).uppercase() }
                                Text(initials, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(state.selectedPatientName, color = textColor, fontWeight = FontWeight.Medium, modifier = Modifier.weight(1f))
                            Icon(Icons.Default.Check, contentDescription = null, tint = primaryPink, modifier = Modifier.size(20.dp))
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    TextButton(onClick = {
                        viewModel.onEvent(AppointmentEvent.SelectPatient("", ""))
                        viewModel.onEvent(AppointmentEvent.SearchPatient(""))
                    }) {
                        Text("Cambiar paciente", color = primaryPink, fontSize = 13.sp)
                    }
                } else {
                    // Search bar
                    OutlinedTextField(
                        value = state.patientSearchQuery,
                        onValueChange = { viewModel.onEvent(AppointmentEvent.SearchPatient(it)) },
                        placeholder = { Text("Buscar por nombre o cédula...", fontSize = 14.sp) },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = subtitleColor) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = primaryPink,
                            unfocusedBorderColor = borderColor,
                            focusedContainerColor = cardBg,
                            unfocusedContainerColor = cardBg
                        )
                    )
                }
            }

            // Patient list (only if no patient selected)
            if (state.selectedPatientId == null || state.selectedPatientId!!.isBlank()) {
                items(state.filteredPatients.take(8)) { patient ->
                    Card(
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(containerColor = cardBg),
                        border = androidx.compose.foundation.BorderStroke(1.dp, borderColor),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                viewModel.onEvent(
                                    AppointmentEvent.SelectPatient(
                                        patient.id,
                                        "${patient.nombre} ${patient.apellidos}"
                                    )
                                )
                            }
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier.size(36.dp).background(Color(0xFF3B82F6).copy(alpha = 0.1f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "${patient.nombre.take(1)}${patient.apellidos.take(1)}".uppercase(),
                                    color = Color(0xFF3B82F6), fontWeight = FontWeight.Bold, fontSize = 12.sp
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text("${patient.nombre} ${patient.apellidos}", color = textColor, fontWeight = FontWeight.Medium, fontSize = 14.sp)
                                Text(if (patient.cedula.isNotBlank()) patient.cedula else "Sin cédula", color = subtitleColor, fontSize = 12.sp)
                            }
                        }
                    }
                }

                if (state.filteredPatients.isEmpty()) {
                    item {
                        Box(modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp), contentAlignment = Alignment.Center) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Default.Person, contentDescription = null, tint = subtitleColor, modifier = Modifier.size(32.dp))
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("No se encontraron pacientes", color = subtitleColor, fontSize = 14.sp)
                            }
                        }
                    }
                }
            }

            // Form fields (visible when patient is selected)
            if (state.selectedPatientId != null && state.selectedPatientId!!.isNotBlank()) {
                item {
                    HorizontalDivider(color = borderColor, thickness = 1.dp)
                }

                // Date & Time
                item {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        // Date
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Fecha", color = textColor, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Spacer(modifier = Modifier.height(6.dp))
                            OutlinedCard(
                                onClick = { showDatePicker = true },
                                shape = RoundedCornerShape(12.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, borderColor),
                                colors = CardDefaults.outlinedCardColors(containerColor = cardBg)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.CalendarToday, contentDescription = null, tint = primaryPink, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        if (state.formDate.isNotBlank()) state.formDate else "Seleccionar",
                                        color = if (state.formDate.isNotBlank()) textColor else subtitleColor,
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }

                        // Time
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Hora", color = textColor, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Spacer(modifier = Modifier.height(6.dp))
                            OutlinedCard(
                                onClick = { showTimePicker = true },
                                shape = RoundedCornerShape(12.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, borderColor),
                                colors = CardDefaults.outlinedCardColors(containerColor = cardBg)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.Schedule, contentDescription = null, tint = primaryPink, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(state.formTime, color = textColor, fontSize = 14.sp)
                                }
                            }
                        }
                    }
                }

                // Type
                item {
                    Text("Tipo de Cita", color = textColor, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    ExposedDropdownMenuBox(
                        expanded = showTypeDropdown,
                        onExpandedChange = { showTypeDropdown = it }
                    ) {
                        OutlinedTextField(
                            value = state.formType,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = showTypeDropdown) },
                            modifier = Modifier.fillMaxWidth().menuAnchor(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = primaryPink,
                                unfocusedBorderColor = borderColor,
                                focusedContainerColor = cardBg,
                                unfocusedContainerColor = cardBg
                            )
                        )
                        ExposedDropdownMenu(
                            expanded = showTypeDropdown,
                            onDismissRequest = { showTypeDropdown = false }
                        ) {
                            appointmentTypes.forEach { type ->
                                DropdownMenuItem(
                                    text = { Text(type) },
                                    onClick = {
                                        viewModel.onEvent(AppointmentEvent.UpdateFormType(type))
                                        showTypeDropdown = false
                                    }
                                )
                            }
                        }
                    }
                }

                // Notes
                item {
                    Text("Notas / Motivo", color = textColor, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = state.formNotes,
                        onValueChange = { viewModel.onEvent(AppointmentEvent.UpdateFormNotes(it)) },
                        placeholder = { Text("Detalles adicionales...", fontSize = 14.sp) },
                        leadingIcon = { Icon(Icons.Outlined.Description, contentDescription = null, tint = subtitleColor) },
                        modifier = Modifier.fillMaxWidth().height(120.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = primaryPink,
                            unfocusedBorderColor = borderColor,
                            focusedContainerColor = cardBg,
                            unfocusedContainerColor = cardBg
                        )
                    )
                }

                // Error
                if (state.formError != null) {
                    item {
                        Text(state.formError!!, color = Color(0xFFEF4444), fontSize = 13.sp, fontWeight = FontWeight.Medium)
                    }
                }

                // Submit
                item {
                    Spacer(modifier = Modifier.height(4.dp))
                    Button(
                        onClick = { viewModel.onEvent(AppointmentEvent.SaveAppointment) },
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = primaryPink)
                    ) {
                        Icon(Icons.Default.CalendarToday, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Agendar Cita", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}
