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
fun EditPatientScreen(
    patientId: String,
    viewModel: EditPatientViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(patientId) {
        viewModel.onEvent(EditPatientEvent.LoadPatient(patientId))
    }

    LaunchedEffect(state.savedSuccessfully) {
        if (state.savedSuccessfully) navigateBack()
    }

    EditPatientBody(state = state, onEvent = viewModel::onEvent, navigateBack = navigateBack)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPatientBody(
    state: EditPatientUiState,
    onEvent: (EditPatientEvent) -> Unit,
    navigateBack: () -> Unit
) {
    val isDark = isSystemInDarkTheme()
    val bgColor = if (isDark) Color(0xFF121212) else Color(0xFFF8F9FA)
    val primaryPink = Color(0xFFEC5C76)
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.userMessage) {
        state.userMessage?.let {
            snackbarHostState.showSnackbar(it)
            onEvent(EditPatientEvent.UserMessageShown)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Editar Paciente", fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    IconButton(
                        onClick = { onEvent(EditPatientEvent.SavePatient) },
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

            SectionTitle("Datos Personales")

            FormField("Nombre *", state.nombre) { onEvent(EditPatientEvent.UpdateNombre(it)) }
            FormField("Apellidos *", state.apellidos) { onEvent(EditPatientEvent.UpdateApellidos(it)) }
            FormField("Cédula", state.cedula) { onEvent(EditPatientEvent.UpdateCedula(it)) }
            FormField("Fecha de Nacimiento", state.fechaNacimiento) { onEvent(EditPatientEvent.UpdateFechaNacimiento(it)) }

            DropdownField("Género", state.genero, listOf("Femenino", "Masculino")) {
                onEvent(EditPatientEvent.UpdateGenero(it))
            }

            SectionTitle("Contacto")

            FormField("Teléfono", state.telefono) { onEvent(EditPatientEvent.UpdateTelefono(it)) }
            FormField("Dirección", state.direccion) { onEvent(EditPatientEvent.UpdateDireccion(it)) }
            FormField("Ciudad", state.ciudad) { onEvent(EditPatientEvent.UpdateCiudad(it)) }

            SectionTitle("Información Médica")

            DropdownField("Tipo de Sangre", state.bloodType, listOf("", "O+", "O-", "A+", "A-", "B+", "B-", "AB+", "AB-")) {
                onEvent(EditPatientEvent.UpdateBloodType(it))
            }

            DropdownField("Estado Civil", state.civilStatus, listOf("", "Soltero/a", "Casado/a", "Divorciado/a", "Viudo/a", "Unión Libre")) {
                onEvent(EditPatientEvent.UpdateCivilStatus(it))
            }

            FormField("ARS / Seguro", state.ars) { onEvent(EditPatientEvent.UpdateArs(it)) }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { onEvent(EditPatientEvent.SavePatient) },
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
                    Text("Guardar Cambios", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    val isDark = isSystemInDarkTheme()
    Text(
        text = title,
        color = if (isDark) Color(0xFFEC5C76) else Color(0xFFEC5C76),
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(top = 8.dp)
    )
    HorizontalDivider(color = Color(0xFFEC5C76).copy(alpha = 0.2f))
}

@Composable
fun FormField(label: String, value: String, onValueChange: (String) -> Unit) {
    val isDark = isSystemInDarkTheme()
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFFEC5C76),
            focusedLabelColor = Color(0xFFEC5C76),
            cursorColor = Color(0xFFEC5C76)
        ),
        singleLine = true
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownField(label: String, selectedValue: String, options: List<String>, onSelect: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
        OutlinedTextField(
            value = selectedValue.ifBlank { "Seleccionar" },
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.fillMaxWidth().menuAnchor(),
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFEC5C76),
                focusedLabelColor = Color(0xFFEC5C76)
            )
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option.ifBlank { "Sin especificar" }) },
                    onClick = {
                        onSelect(option)
                        expanded = false
                    }
                )
            }
        }
    }
}
