package ucne.edu.ginecosys.presentation.checkout

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Person
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
fun CheckoutScreen(
    appointmentId: String,
    viewModel: CheckoutViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(appointmentId) {
        viewModel.onEvent(CheckoutEvent.LoadAppointment(appointmentId))
    }

    LaunchedEffect(state.paymentCompleted) {
        if (state.paymentCompleted) navigateBack()
    }

    CheckoutBody(state = state, onEvent = viewModel::onEvent, navigateBack = navigateBack)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutBody(
    state: CheckoutUiState,
    onEvent: (CheckoutEvent) -> Unit,
    navigateBack: () -> Unit
) {
    val isDark = isSystemInDarkTheme()
    val bgColor = if (isDark) Color(0xFF121212) else Color(0xFFF8F9FA)
    val textColor = if (isDark) Color.White else Color(0xFF0A2342)
    val subtitleColor = if (isDark) Color.LightGray else Color(0xFF6B7280)
    val greenAccent = Color(0xFF059669)
    val cardBg = if (isDark) Color(0xFF1E1E1E) else Color.White
    val borderColor = if (isDark) Color(0xFF333333) else Color(0xFFE0E0E0)
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.userMessage) {
        state.userMessage?.let {
            snackbarHostState.showSnackbar(it)
            onEvent(CheckoutEvent.UserMessageShown)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Checkout", fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = bgColor)
            )
        },
        containerColor = bgColor
    ) { paddingValues ->
        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = greenAccent)
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Appointment Summary Card
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = cardBg),
                border = androidx.compose.foundation.BorderStroke(1.dp, borderColor),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Person, contentDescription = null, tint = greenAccent, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(state.patientName, color = textColor, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            Text("${state.appointmentType} • ${state.appointmentDate}", color = subtitleColor, fontSize = 13.sp)
                        }
                    }
                }
            }

            SectionTitle("Detalles del Pago")

            OutlinedTextField(
                value = state.amount,
                onValueChange = { onEvent(CheckoutEvent.UpdateAmount(it)) },
                label = { Text("Monto (RD$) *") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = greenAccent,
                    focusedLabelColor = greenAccent,
                    cursorColor = greenAccent
                ),
                singleLine = true,
                prefix = { Text("RD$ ", color = subtitleColor) }
            )

            DropdownField(
                label = "Método de Pago",
                selectedValue = state.method,
                options = listOf("Efectivo", "Tarjeta de Crédito", "Tarjeta de Débito", "Transferencia", "Cheque", "Seguro/ARS")
            ) { onEvent(CheckoutEvent.UpdateMethod(it)) }

            OutlinedTextField(
                value = state.notes,
                onValueChange = { onEvent(CheckoutEvent.UpdateNotes(it)) },
                label = { Text("Notas (opcional)") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = greenAccent,
                    focusedLabelColor = greenAccent,
                    cursorColor = greenAccent
                ),
                minLines = 2,
                maxLines = 4
            )

            // Summary
            if (state.amount.isNotBlank()) {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = greenAccent.copy(alpha = 0.05f)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, greenAccent.copy(alpha = 0.2f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Total", color = textColor, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            Text("RD$ ${state.amount}", color = greenAccent, fontSize = 20.sp, fontWeight = FontWeight.Black)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Método: ${state.method}", color = subtitleColor, fontSize = 13.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { onEvent(CheckoutEvent.ProcessPayment) },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = greenAccent),
                enabled = !state.isProcessing
            ) {
                if (state.isProcessing) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White, strokeWidth = 2.dp)
                } else {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Procesar Pago", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
