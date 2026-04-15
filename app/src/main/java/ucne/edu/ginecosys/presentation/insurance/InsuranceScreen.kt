package ucne.edu.ginecosys.presentation.insurance

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Shield
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
import ucne.edu.ginecosys.domain.model.Insurance
import ucne.edu.ginecosys.presentation.patients.edit.DropdownField
import ucne.edu.ginecosys.presentation.patients.edit.FormField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsuranceScreen(
    viewModel: InsuranceViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val isDark = isSystemInDarkTheme()
    val bgColor = if (isDark) Color(0xFF121212) else Color(0xFFF8F9FA)
    val textColor = if (isDark) Color.White else Color(0xFF0A2342)
    val subtitleColor = if (isDark) Color.LightGray else Color(0xFF6B7280)
    val primaryPink = Color(0xFFEC5C76)
    val cardBg = if (isDark) Color(0xFF1E1E1E) else Color.White
    val borderColor = if (isDark) Color(0xFF333333) else Color(0xFFE0E0E0)
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.userMessage) {
        state.userMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.onEvent(InsuranceEvent.UserMessageShown)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = bgColor
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Header
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Spacer(modifier = Modifier.height(24.dp))
                Text("Gestión de ARS / Seguros", color = textColor, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text("Administra las aseguradoras disponibles.", color = subtitleColor, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = { viewModel.onEvent(InsuranceEvent.ShowAddDialog) },
                    modifier = Modifier.height(48.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = primaryPink)
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Nuevo", tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Nuevo Seguro", color = Color.White, fontWeight = FontWeight.SemiBold)
                }

                Spacer(modifier = Modifier.height(24.dp))
            }

            Card(
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                colors = CardDefaults.cardColors(containerColor = cardBg),
                border = androidx.compose.foundation.BorderStroke(1.dp, borderColor),
                modifier = Modifier.fillMaxWidth().weight(1f)
            ) {
                if (state.insurances.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                modifier = Modifier
                                    .size(72.dp)
                                    .background(if (isDark) Color(0xFF333333) else Color(0xFFF3F4F6), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Shield, contentDescription = null, modifier = Modifier.size(36.dp), tint = Color.Gray)
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Text("No hay seguros registrados", color = textColor, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            Text("Haz clic en 'Nuevo Seguro' para agregar.", color = subtitleColor, fontSize = 14.sp)
                        }
                    }
                } else {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(state.insurances) { insurance ->
                            InsuranceListItem(
                                insurance = insurance,
                                textColor = textColor,
                                subtitleColor = subtitleColor,
                                borderColor = borderColor,
                                primaryPink = primaryPink,
                                onEdit = { viewModel.onEvent(InsuranceEvent.SelectForEdit(it)) },
                                onToggle = { viewModel.onEvent(InsuranceEvent.ToggleActive(it)) },
                                onDelete = { viewModel.onEvent(InsuranceEvent.DeleteInsurance(it.id)) }
                            )
                        }
                    }
                }
            }
        }

        // Add/Edit Dialog
        if (state.showDialog) {
            InsuranceDialog(
                state = state,
                onEvent = viewModel::onEvent,
                onDismiss = { viewModel.onEvent(InsuranceEvent.HideDialog) }
            )
        }
    }
}

@Composable
private fun InsuranceListItem(
    insurance: Insurance,
    textColor: Color,
    subtitleColor: Color,
    borderColor: Color,
    primaryPink: Color,
    onEdit: (Insurance) -> Unit,
    onToggle: (Insurance) -> Unit,
    onDelete: (Insurance) -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onEdit(insurance) }
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(
                        if (insurance.isActive) primaryPink.copy(alpha = 0.1f) else Color.Gray.copy(alpha = 0.1f),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Shield,
                    contentDescription = null,
                    tint = if (insurance.isActive) primaryPink else Color.Gray,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(insurance.name, color = textColor, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                if (insurance.type.isNotBlank()) {
                    Text(insurance.type, color = subtitleColor, fontSize = 13.sp)
                }
                if (insurance.phone.isNotBlank()) {
                    Text("Tel: ${insurance.phone}", color = subtitleColor, fontSize = 12.sp)
                }
            }

            Switch(
                checked = insurance.isActive,
                onCheckedChange = { onToggle(insurance) },
                colors = SwitchDefaults.colors(checkedTrackColor = primaryPink),
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            IconButton(onClick = { onEdit(insurance) }) {
                Icon(Icons.Default.Edit, contentDescription = "Editar", tint = subtitleColor, modifier = Modifier.size(20.dp))
            }

            IconButton(onClick = { onDelete(insurance) }) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color(0xFFDC2626), modifier = Modifier.size(20.dp))
            }
        }
        HorizontalDivider(color = borderColor, thickness = 1.dp)
    }
}

@Composable
private fun InsuranceDialog(
    state: InsuranceUiState,
    onEvent: (InsuranceEvent) -> Unit,
    onDismiss: () -> Unit
) {
    val primaryPink = Color(0xFFEC5C76)
    val isEditing = state.editingInsurance != null

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (isEditing) "Editar Seguro" else "Nuevo Seguro", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                FormField("Nombre del Seguro *", state.name) { onEvent(InsuranceEvent.UpdateName(it)) }
                DropdownField(
                    label = "Tipo",
                    selectedValue = state.type,
                    options = listOf("", "ARS", "Seguro Privado", "Plan Básico", "Plan Complementario")
                ) { onEvent(InsuranceEvent.UpdateType(it)) }
                FormField("Teléfono", state.phone) { onEvent(InsuranceEvent.UpdatePhone(it)) }
            }
        },
        confirmButton = {
            Button(
                onClick = { onEvent(InsuranceEvent.SaveInsurance) },
                colors = ButtonDefaults.buttonColors(containerColor = primaryPink),
                enabled = !state.isSaving
            ) {
                Text(if (isEditing) "Actualizar" else "Crear", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
