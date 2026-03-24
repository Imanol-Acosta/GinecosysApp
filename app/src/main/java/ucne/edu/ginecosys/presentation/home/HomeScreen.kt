package ucne.edu.ginecosys.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.GroupAdd
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    goToDetail: () -> Unit
) {
    val isDark = isSystemInDarkTheme()
    val bgColor = if (isDark) Color(0xFF121212) else Color(0xFFF8F9FA)
    val textColor = if (isDark) Color.White else Color(0xFF0A2342)
    val primaryPink = Color(0xFFEC5C76)
    val subtitleColor = if (isDark) Color.LightGray else Color.Gray
    val cardBg = if (isDark) Color(0xFF1E1E1E) else Color.White
    val borderColor = if (isDark) Color(0xFF333333) else Color(0xFFE0E0E0)
    
    val currentDate = SimpleDateFormat("EEEE, d 'De' MMMM 'De' yyyy", Locale("es", "ES")).format(Date()).let { 
        it.replaceFirstChar { char -> char.uppercase() } 
    }

    Scaffold(
        containerColor = bgColor
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Buenos días, Doctor",
                    color = textColor,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = currentDate,
                    color = subtitleColor,
                    fontSize = 14.sp
                )
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StatCard(
                            modifier = Modifier.weight(1f),
                            title = "CITAS HOY",
                            value = "0",
                            icon = Icons.Outlined.CalendarMonth,
                            iconTint = primaryPink,
                            iconBg = primaryPink.copy(alpha = 0.1f),
                            cardBg = cardBg,
                            borderColor = borderColor,
                            textColor = textColor
                        )
                        StatCard(
                            modifier = Modifier.weight(1f),
                            title = "PACIENTES",
                            value = "0",
                            icon = Icons.Filled.People,
                            iconTint = Color(0xFF3B82F6),
                            iconBg = Color(0xFF3B82F6).copy(alpha = 0.1f),
                            cardBg = cardBg,
                            borderColor = borderColor,
                            textColor = textColor
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StatCard(
                            modifier = Modifier.weight(1f),
                            title = "INGRESOS",
                            value = "RD$0",
                            icon = Icons.Filled.AttachMoney,
                            iconTint = Color(0xFF10B981),
                            iconBg = Color(0xFF10B981).copy(alpha = 0.1f),
                            cardBg = cardBg,
                            borderColor = borderColor,
                            textColor = textColor
                        )
                        StatCard(
                            modifier = Modifier.weight(1f),
                            title = "PENDIENTES",
                            value = "0",
                            icon = Icons.Filled.Schedule,
                            iconTint = Color(0xFFF59E0B),
                            iconBg = Color(0xFFF59E0B).copy(alpha = 0.1f),
                            cardBg = cardBg,
                            borderColor = borderColor,
                            textColor = textColor
                        )
                    }
                }
            }

            item {
                Text(
                    text = "Accesos Rápidos",
                    color = textColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Button(
                    onClick = { /* TODO */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = primaryPink)
                ) {
                    Icon(Icons.Filled.Add, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Nueva Cita", color = Color.White, fontWeight = FontWeight.SemiBold)
                }
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedButton(
                    onClick = { /* TODO */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = textColor),
                    border = androidx.compose.foundation.BorderStroke(1.dp, borderColor)
                ) {
                    Icon(Icons.Outlined.GroupAdd, contentDescription = null, tint = textColor)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Nuevo Paciente", color = textColor, fontWeight = FontWeight.SemiBold)
                }
            }

            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = cardBg),
                    border = androidx.compose.foundation.BorderStroke(1.dp, borderColor),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Outlined.CalendarMonth, contentDescription = null, tint = primaryPink, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Agenda del Día", color = textColor, fontWeight = FontWeight.Bold)
                            }
                            Text(
                                "Ver todo >",
                                color = primaryPink,
                                fontSize = 12.sp,
                                modifier = Modifier.clickable { }
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(32.dp))
                        
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .background(if (isDark) Color(0xFF2A2A2A) else Color(0xFFF3F4F6), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Outlined.CalendarMonth, contentDescription = null, tint = Color.Gray)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("No hay citas programadas", color = textColor, fontWeight = FontWeight.Bold)
                        Text("Hoy no tienes pacientes agendados.", color = subtitleColor, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("+ Agendar Cita", color = primaryPink, fontWeight = FontWeight.Medium, modifier = Modifier.clickable { })
                        
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }

            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = cardBg),
                    border = androidx.compose.foundation.BorderStroke(1.dp, borderColor),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Filled.People, contentDescription = null, tint = primaryPink, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Recientes", color = textColor, fontWeight = FontWeight.Bold)
                            }
                            Text(
                                "Ver Todos",
                                color = primaryPink,
                                fontSize = 12.sp,
                                modifier = Modifier.clickable { }
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .border(1.dp, Color.LightGray, CircleShape)
                                        .clickable { },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Filled.Add, contentDescription = "Add Recent", tint = Color.Gray)
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun StatCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    icon: ImageVector,
    iconTint: Color,
    iconBg: Color,
    cardBg: Color,
    borderColor: Color,
    textColor: Color
) {
    Card(
        modifier = modifier.height(90.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        border = androidx.compose.foundation.BorderStroke(1.dp, borderColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = title,
                    color = Color.Gray,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 4.dp)
                )
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .background(iconBg, RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(16.dp))
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = value,
                color = textColor,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
