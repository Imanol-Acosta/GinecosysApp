package ucne.edu.ginecosys.presentation.patients.edit

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditClinicalDataScreen(
    patientId: String,
    viewModel: EditClinicalViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(patientId) {
        viewModel.onEvent(EditClinicalEvent.LoadPatient(patientId))
    }

    LaunchedEffect(state.savedSuccessfully) {
        if (state.savedSuccessfully) navigateBack()
    }

    EditClinicalBody(state = state, onEvent = viewModel::onEvent, navigateBack = navigateBack)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditClinicalBody(
    state: EditClinicalUiState,
    onEvent: (EditClinicalEvent) -> Unit,
    navigateBack: () -> Unit
) {
    val isDark = isSystemInDarkTheme()
    val bgColor = if (isDark) Color(0xFF121212) else Color(0xFFF8F9FA)
    val primaryPink = Color(0xFFEC5C76)
    val textColor = if (isDark) Color.White else Color(0xFF0A2342)
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.userMessage) {
        state.userMessage?.let {
            snackbarHostState.showSnackbar(it)
            onEvent(EditClinicalEvent.UserMessageShown)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Perfil Ginecológico", fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    IconButton(
                        onClick = { onEvent(EditClinicalEvent.SaveClinical) },
                        enabled = !state.isSaving
                    ) {
                        if (state.isSaving) {
                            CircularProgressIndicator(modifier = Modifier.size(20.dp), color = primaryPink, strokeWidth = 2.dp)
                        } else {
                            Icon(Icons.Default.Save, contentDescription = "Guardar", tint = primaryPink)
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

            SectionTitle("Datos Ginecológicos")

            FormField("F.U.M. (Fecha Última Menstruación)", state.fum) {
                onEvent(EditClinicalEvent.UpdateFum(it))
            }

            FormField("Ciclo (ej: 28/5)", state.ciclo) {
                onEvent(EditClinicalEvent.UpdateCiclo(it))
            }

            FormField("Menarquía (edad en años)", state.menarquia) {
                onEvent(EditClinicalEvent.UpdateMenarquia(it))
            }

            DropdownField(
                label = "Planificación",
                selectedValue = state.planificacion,
                options = listOf("", "Ninguna", "ACO", "DIU", "Inyectable", "Condón", "Ligadura", "Implante", "Otro")
            ) { onEvent(EditClinicalEvent.UpdatePlanificacion(it)) }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Vida Sexual Activa", color = textColor, fontSize = 14.sp, modifier = Modifier.weight(1f))
                Switch(
                    checked = state.sexualActivity,
                    onCheckedChange = { onEvent(EditClinicalEvent.UpdateSexualActivity(it)) },
                    colors = SwitchDefaults.colors(checkedTrackColor = primaryPink)
                )
            }

            SectionTitle("Historia Obstétrica")

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ObstetricField("G (Gestaciones)", state.g, Modifier.weight(1f)) { onEvent(EditClinicalEvent.UpdateG(it)) }
                ObstetricField("P (Partos)", state.p, Modifier.weight(1f)) { onEvent(EditClinicalEvent.UpdateP(it)) }
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ObstetricField("C (Cesáreas)", state.c, Modifier.weight(1f)) { onEvent(EditClinicalEvent.UpdateC(it)) }
                ObstetricField("A (Abortos)", state.a, Modifier.weight(1f)) { onEvent(EditClinicalEvent.UpdateA(it)) }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { onEvent(EditClinicalEvent.SaveClinical) },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primaryPink),
                enabled = !state.isSaving
            ) {
                if (state.isSaving) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White, strokeWidth = 2.dp)
                } else {
                    Icon(Icons.Default.Save, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Guardar Datos Clínicos", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun ObstetricField(label: String, value: String, modifier: Modifier = Modifier, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = { newVal -> onValueChange(newVal.filter { it.isDigit() }) },
        label = { Text(label, fontSize = 12.sp) },
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFFEC5C76),
            focusedLabelColor = Color(0xFFEC5C76),
            cursorColor = Color(0xFFEC5C76)
        ),
        singleLine = true
    )
}
