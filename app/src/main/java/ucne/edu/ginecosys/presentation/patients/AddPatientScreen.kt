package ucne.edu.ginecosys.presentation.patients

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun AddPatientScreen(
    viewModel: PatientViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    
    // We navigate back as soon as the form finishes successfully without errors, 
    // but a cleaner way is observing if currentPatient was cleared after a save.
    // For simplicity, we trigger navigateBack from the save button onClick immediately 
    // if required fields are filled.
    
    AddPatientBody(
        state = state,
        onEvent = viewModel::onEvent,
        navigateBack = navigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPatientBody(
    state: PatientUiState,
    onEvent: (PatientEvent) -> Unit,
    navigateBack: () -> Unit
) {
    val isDark = isSystemInDarkTheme()
    val bgColor = if (isDark) Color(0xFF121212) else Color(0xFFF8F9FA)
    val textColor = if (isDark) Color.White else Color(0xFF0A2342)
    val primaryPink = Color(0xFFEC5C76)
    val cardBg = if (isDark) Color(0xFF1E1E1E) else Color.White

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nuevo Paciente", fontWeight = FontWeight.Bold, color = textColor) },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = textColor)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = bgColor)
            )
        },
        containerColor = bgColor
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Text("Información Personal", color = primaryPink, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)

            OutlinedTextField(
                value = state.nombre,
                onValueChange = { onEvent(PatientEvent.UpdateNombre(it)) },
                label = { Text("Nombre (Obligatorio)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = state.showError && state.nombre.isBlank(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = primaryPink,
                    focusedLabelColor = primaryPink,
                    unfocusedContainerColor = cardBg,
                    focusedContainerColor = cardBg,
                    errorContainerColor = cardBg
                )
            )

            OutlinedTextField(
                value = state.apellidos,
                onValueChange = { onEvent(PatientEvent.UpdateApellidos(it)) },
                label = { Text("Apellidos (Obligatorio)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = state.showError && state.apellidos.isBlank(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = primaryPink,
                    focusedLabelColor = primaryPink,
                    unfocusedContainerColor = cardBg,
                    focusedContainerColor = cardBg,
                    errorContainerColor = cardBg
                )
            )

            OutlinedTextField(
                value = state.cedula,
                onValueChange = { onEvent(PatientEvent.UpdateCedula(it)) },
                label = { Text("Cédula (Opcional)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = primaryPink,
                    focusedLabelColor = primaryPink,
                    unfocusedContainerColor = cardBg,
                    focusedContainerColor = cardBg
                )
            )

            OutlinedTextField(
                value = state.fechaNacimiento,
                onValueChange = { onEvent(PatientEvent.UpdateFechaNacimiento(it)) },
                label = { Text("Fecha de Nacimiento (Obligatorio)") },
                placeholder = { Text("DD/MM/YYYY") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                singleLine = true,
                isError = state.showError && state.fechaNacimiento.isBlank(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = primaryPink,
                    focusedLabelColor = primaryPink,
                    unfocusedContainerColor = cardBg,
                    focusedContainerColor = cardBg,
                    errorContainerColor = cardBg
                )
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            Text("Contacto y Seguro", color = primaryPink, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)

            OutlinedTextField(
                value = state.ars,
                onValueChange = { onEvent(PatientEvent.UpdateArs(it)) },
                label = { Text("ARS (Opcional)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = primaryPink,
                    focusedLabelColor = primaryPink,
                    unfocusedContainerColor = cardBg,
                    focusedContainerColor = cardBg
                )
            )

            OutlinedTextField(
                value = state.telefono,
                onValueChange = { onEvent(PatientEvent.UpdateTelefono(it)) },
                label = { Text("Número de teléfono (Opcional)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = primaryPink,
                    focusedLabelColor = primaryPink,
                    unfocusedContainerColor = cardBg,
                    focusedContainerColor = cardBg
                )
            )

            OutlinedTextField(
                value = state.direccion,
                onValueChange = { onEvent(PatientEvent.UpdateDireccion(it)) },
                label = { Text("Dirección (Opcional)") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = primaryPink,
                    focusedLabelColor = primaryPink,
                    unfocusedContainerColor = cardBg,
                    focusedContainerColor = cardBg
                )
            )

            if (state.showError) {
                Text("Por favor, complete todos los campos obligatorios.", color = Color.Red, fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    onEvent(PatientEvent.SavePatient)
                    if (state.nombre.isNotBlank() && state.apellidos.isNotBlank() && state.fechaNacimiento.isNotBlank()) {
                        navigateBack()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primaryPink)
            ) {
                Icon(Icons.Filled.Save, contentDescription = "Guardar", tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Guardar Paciente", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
