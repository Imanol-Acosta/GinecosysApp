package ucne.edu.ginecosys.presentation.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ucne.edu.ginecosys.R

import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onLoginSuccess: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onLoginSuccess()
        }
    }

    val isDark = isSystemInDarkTheme()
    val backgroundColor = if (isDark) Color(0xFF121212) else Color.White
    val textColorDarkBlue = if (isDark) Color.White else Color(0xFF0A2342)
    val primaryPink = Color(0xFFEC5C76) 

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(64.dp))
        
        // Logo
        Image(
            painter = painterResource(id = R.mipmap.ic_launcher),
            contentDescription = "GinecoSys Logo",
            modifier = Modifier.size(120.dp),
            contentScale = ContentScale.Fit
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Title
        Text(
            text = "GinecoSys",
            color = textColorDarkBlue,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Subtitle
        Text(
            text = "Gestión Clínica Para Ginecólogos",
            color = primaryPink,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )
        
        Spacer(modifier = Modifier.height(48.dp))
        
        // Email Input
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Correo Electrónico",
                color = textColorDarkBlue,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            OutlinedTextField(
                value = uiState.email,
                onValueChange = { viewModel.onEvent(LoginEvent.OnEmailChanged(it)) },
                placeholder = { Text("doctor@clinica.com", color = Color.Gray) },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Email, contentDescription = "Email", tint = Color.Gray)
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = primaryPink,
                    unfocusedBorderColor = Color(0xFFEEEEEE),
                    focusedContainerColor = if (isDark) Color(0xFF1E1E1E) else Color(0xFFF9F9F9),
                    unfocusedContainerColor = if (isDark) Color(0xFF1E1E1E) else Color(0xFFF9F9F9)
                )
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Password Input
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Contraseña",
                color = textColorDarkBlue,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            OutlinedTextField(
                value = uiState.password,
                onValueChange = { viewModel.onEvent(LoginEvent.OnPasswordChanged(it)) },
                placeholder = { Text("........", color = Color.Gray) },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Lock, contentDescription = "Lock", tint = Color.Gray)
                },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = primaryPink,
                    unfocusedBorderColor = Color(0xFFEEEEEE),
                    focusedContainerColor = if (isDark) Color(0xFF1E1E1E) else Color(0xFFF9F9F9),
                    unfocusedContainerColor = if (isDark) Color(0xFF1E1E1E) else Color(0xFFF9F9F9)
                )
            )
        }
        
        if (uiState.error != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = uiState.error!!, color = Color.Red, fontSize = 14.sp)
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Login Button
        Button(
            onClick = { viewModel.onEvent(LoginEvent.Submit) },
            enabled = !uiState.isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = primaryPink)
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Login,
                    contentDescription = "Login",
                    modifier = Modifier.padding(end = 8.dp),
                    tint = Color.White
                )
                Text(
                    text = "Iniciar Sesión",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        HorizontalDivider(color = Color.LightGray, thickness = 0.5.dp, modifier = Modifier.padding(vertical = 16.dp))
        
        // Footer
        Text(
            text = "© 2026 GinecoSys • Powered by EfresTech",
            color = Color.Gray,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )
    }
}
