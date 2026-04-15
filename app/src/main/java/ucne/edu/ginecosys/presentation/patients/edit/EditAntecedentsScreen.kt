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
fun EditAntecedentsScreen(
    patientId: String,
    viewModel: EditAntecedentsViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(patientId) {
        viewModel.onEvent(EditAntecedentsEvent.LoadPatient(patientId))
    }

    LaunchedEffect(state.savedSuccessfully) {
        if (state.savedSuccessfully) navigateBack()
    }

    EditAntecedentsBody(state = state, onEvent = viewModel::onEvent, navigateBack = navigateBack)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditAntecedentsBody(
    state: EditAntecedentsUiState,
    onEvent: (EditAntecedentsEvent) -> Unit,
    navigateBack: () -> Unit
) {
    val isDark = isSystemInDarkTheme()
    val bgColor = if (isDark) Color(0xFF121212) else Color(0xFFF8F9FA)
    val primaryPink = Color(0xFFEC5C76)
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.userMessage) {
        state.userMessage?.let {
            snackbarHostState.showSnackbar(it)
            onEvent(EditAntecedentsEvent.UserMessageShown)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Antecedentes", fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    IconButton(
                        onClick = { onEvent(EditAntecedentsEvent.SaveAntecedents) },
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

            SectionTitle("Antecedentes del Paciente")

            MultiLineField("Heredo-Familiares", state.family) {
                onEvent(EditAntecedentsEvent.UpdateFamily(it))
            }

            MultiLineField("Patológicos", state.pathological) {
                onEvent(EditAntecedentsEvent.UpdatePathological(it))
            }

            MultiLineField("Alergias", state.allergies) {
                onEvent(EditAntecedentsEvent.UpdateAllergies(it))
            }

            MultiLineField("Quirúrgicos", state.surgical) {
                onEvent(EditAntecedentsEvent.UpdateSurgical(it))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { onEvent(EditAntecedentsEvent.SaveAntecedents) },
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
                    Text("Guardar Antecedentes", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun MultiLineField(label: String, value: String, onValueChange: (String) -> Unit) {
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
        minLines = 3,
        maxLines = 5
    )
}
