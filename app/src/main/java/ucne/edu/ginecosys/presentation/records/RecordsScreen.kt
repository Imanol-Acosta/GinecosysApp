package ucne.edu.ginecosys.presentation.records

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FilterAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ucne.edu.ginecosys.domain.model.Consultation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordsScreen(
    viewModel: RecordsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val isDark = isSystemInDarkTheme()
    val bgColor = if (isDark) Color(0xFF121212) else Color(0xFFF8F9FA)
    val textColor = if (isDark) Color.White else Color(0xFF0A2342)
    val subtitleColor = if (isDark) Color.LightGray else Color(0xFF6B7280)
    val primaryPink = Color(0xFFEC5C76)
    val cardBg = if (isDark) Color(0xFF1E1E1E) else Color.White
    val borderColor = if (isDark) Color(0xFF333333) else Color(0xFFE0E0E0)

    Scaffold(containerColor = bgColor) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues)
        ) {
            Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp)) {
                Spacer(modifier = Modifier.height(24.dp))
                Text("Récords Clínicos", color = textColor, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text("Historial global de consultas médicas.", color = subtitleColor, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(20.dp))
            }

            Card(
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                colors = CardDefaults.cardColors(containerColor = cardBg),
                border = androidx.compose.foundation.BorderStroke(1.dp, borderColor),
                modifier = Modifier.fillMaxWidth().weight(1f)
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Column(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
                        OutlinedTextField(
                            value = state.searchQuery,
                            onValueChange = { viewModel.onEvent(RecordsEvent.UpdateSearch(it)) },
                            placeholder = { Text("Buscar por paciente, diagnóstico...", color = Color.Gray) },
                            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Search", tint = Color.Gray) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = primaryPink,
                                unfocusedBorderColor = borderColor
                            )
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            val filters = listOf("", "General", "Prenatal", "Ginecológica", "Urgencia")
                            filters.forEach { filter ->
                                val isSelected = state.filterType == filter
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { viewModel.onEvent(RecordsEvent.UpdateFilter(filter)) },
                                    label = { Text(filter.ifBlank { "Todos" }, fontSize = 12.sp) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = primaryPink,
                                        selectedLabelColor = Color.White
                                    )
                                )
                            }
                        }
                    }

                    HorizontalDivider(color = borderColor, thickness = 1.dp)

                    if (state.filteredConsultations.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Box(
                                    modifier = Modifier
                                        .size(72.dp)
                                        .background(if (isDark) Color(0xFF333333) else Color(0xFFF3F4F6), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.Description, contentDescription = null, modifier = Modifier.size(36.dp), tint = Color.Gray)
                                }
                                Spacer(modifier = Modifier.height(16.dp))
                                Text("No hay récords", color = textColor, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                Text(
                                    "Las consultas registradas aparecerán aquí.",
                                    color = subtitleColor, fontSize = 14.sp, textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(horizontal = 32.dp, vertical = 8.dp)
                                )
                            }
                        }
                    } else {
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            items(state.filteredConsultations) { consultation ->
                                val patient = state.patients[consultation.patientId]
                                val patientName = "${patient?.nombre ?: "?"} ${patient?.apellidos ?: ""}"

                                RecordListItem(
                                    consultation = consultation,
                                    patientName = patientName,
                                    textColor = textColor,
                                    subtitleColor = subtitleColor,
                                    borderColor = borderColor,
                                    primaryPink = primaryPink
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
private fun RecordListItem(
    consultation: Consultation,
    patientName: String,
    textColor: Color,
    subtitleColor: Color,
    borderColor: Color,
    primaryPink: Color
) {
    Column {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .background(primaryPink.copy(alpha = 0.1f), RoundedCornerShape(4.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(consultation.type.uppercase(), color = primaryPink, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
                Text(consultation.date, color = subtitleColor, fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(patientName, color = textColor, fontWeight = FontWeight.Bold, fontSize = 15.sp)

            if (consultation.reason.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text("Motivo: ${consultation.reason}", color = subtitleColor, fontSize = 13.sp, maxLines = 2)
            }

            if (consultation.diagnosis.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text("Dx: ${consultation.diagnosis}", color = textColor, fontSize = 13.sp, fontWeight = FontWeight.Medium, maxLines = 2)
            }
        }
        HorizontalDivider(color = borderColor, thickness = 1.dp)
    }
}
