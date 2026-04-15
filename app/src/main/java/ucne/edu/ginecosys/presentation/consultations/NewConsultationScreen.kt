package ucne.edu.ginecosys.presentation.consultations

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
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
import ucne.edu.ginecosys.presentation.patients.edit.DropdownField
import ucne.edu.ginecosys.presentation.patients.edit.FormField
import ucne.edu.ginecosys.presentation.patients.edit.SectionTitle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewConsultationScreen(
    patientId: String,
    viewModel: NewConsultationViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(patientId) {
        viewModel.onEvent(ConsultationEvent.LoadForPatient(patientId))
    }

    LaunchedEffect(state.savedSuccessfully) {
        if (state.savedSuccessfully) navigateBack()
    }

    NewConsultationBody(state = state, onEvent = viewModel::onEvent, navigateBack = navigateBack)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewConsultationBody(
    state: ConsultationUiState,
    onEvent: (ConsultationEvent) -> Unit,
    navigateBack: () -> Unit
) {
    val isDark = isSystemInDarkTheme()
    val bgColor = if (isDark) Color(0xFF121212) else Color(0xFFF8F9FA)
    val primaryPink = Color(0xFFEC5C76)
    val greenAccent = Color(0xFF059669)
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.userMessage) {
        state.userMessage?.let {
            snackbarHostState.showSnackbar(it)
            onEvent(ConsultationEvent.UserMessageShown)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Nueva Consulta", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        if (state.patientName.isNotBlank()) {
                            Text(state.patientName, fontSize = 13.sp, color = if (isDark) Color.LightGray else Color.Gray)
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    IconButton(
                        onClick = { onEvent(ConsultationEvent.SaveConsultation) },
                        enabled = !state.isSaving
                    ) {
                        if (state.isSaving) {
                            CircularProgressIndicator(modifier = Modifier.size(20.dp), color = primaryPink, strokeWidth = 2.dp)
                        } else {
                            Icon(Icons.Default.Save, contentDescription = "Guardar", tint = greenAccent)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = bgColor)
            )
        },
        containerColor = bgColor
    ) { paddingValues ->
        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = primaryPink)
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            SectionTitle("Información General")

            FormField("Fecha", state.date) { onEvent(ConsultationEvent.UpdateDate(it)) }

            DropdownField(
                label = "Tipo de Consulta",
                selectedValue = state.type,
                options = listOf("General", "Prenatal", "Posparto", "Ginecológica", "Urgencia", "Seguimiento")
            ) { onEvent(ConsultationEvent.UpdateType(it)) }

            SectionTitle("Datos Clínicos")

            MultiLineConsultField("Motivo de Consulta *", state.reason) {
                onEvent(ConsultationEvent.UpdateReason(it))
            }

            MultiLineConsultField("Examen Físico", state.exam) {
                onEvent(ConsultationEvent.UpdateExam(it))
            }

            MultiLineConsultField("Diagnóstico", state.diagnosis) {
                onEvent(ConsultationEvent.UpdateDiagnosis(it))
            }

            MultiLineConsultField("Plan / Tratamiento", state.plan) {
                onEvent(ConsultationEvent.UpdatePlan(it))
            }

            SectionTitle("Signos Vitales")

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                VitalField("PA (mmHg)", state.vitals.bp, Modifier.weight(1f)) {
                    onEvent(ConsultationEvent.UpdateVitalsBp(it))
                }
                VitalField("Peso (kg)", state.vitals.weight, Modifier.weight(1f)) {
                    onEvent(ConsultationEvent.UpdateVitalsWeight(it))
                }
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                VitalField("FC (bpm)", state.vitals.hr, Modifier.weight(1f)) {
                    onEvent(ConsultationEvent.UpdateVitalsHr(it))
                }
                VitalField("Temp (°C)", state.vitals.temp, Modifier.weight(1f)) {
                    onEvent(ConsultationEvent.UpdateVitalsTemp(it))
                }
            }

            VitalField("FCF (bpm)", state.vitals.fhr, Modifier.fillMaxWidth()) {
                onEvent(ConsultationEvent.UpdateVitalsFhr(it))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { onEvent(ConsultationEvent.SaveConsultation) },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = greenAccent),
                enabled = !state.isSaving
            ) {
                if (state.isSaving) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White, strokeWidth = 2.dp)
                } else {
                    Icon(Icons.Default.Save, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Guardar Consulta", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun MultiLineConsultField(label: String, value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF059669),
            focusedLabelColor = Color(0xFF059669),
            cursorColor = Color(0xFF059669)
        ),
        minLines = 3,
        maxLines = 6
    )
}

@Composable
private fun VitalField(label: String, value: String, modifier: Modifier = Modifier, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, fontSize = 12.sp) },
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF059669),
            focusedLabelColor = Color(0xFF059669),
            cursorColor = Color(0xFF059669)
        ),
        singleLine = true
    )
}
