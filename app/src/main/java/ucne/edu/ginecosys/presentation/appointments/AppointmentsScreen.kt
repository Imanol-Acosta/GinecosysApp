package ucne.edu.ginecosys.presentation.appointments

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentsScreen(
    viewModel: AppointmentViewModel = hiltViewModel(),
    goToAddAppointment: () -> Unit,
    goToDetail: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val isDark = isSystemInDarkTheme()
    val bgColor = if (isDark) Color(0xFF121212) else Color(0xFFF8F9FA)
    val textColor = if (isDark) Color.White else Color(0xFF0A2342)
    val primaryPink = Color(0xFFEC5C76)
    val subtitleColor = if (isDark) Color.LightGray else Color(0xFF6B7280)
    val cardBg = if (isDark) Color(0xFF1E1E1E) else Color.White
    val borderColor = if (isDark) Color(0xFF333333) else Color(0xFFE0E0E0)

    val monthName = SimpleDateFormat("MMMM yyyy", Locale("es", "ES"))
        .format(Calendar.getInstance().apply {
            set(Calendar.YEAR, uiState.currentYear)
            set(Calendar.MONTH, uiState.currentMonth)
            set(Calendar.DAY_OF_MONTH, 1)
        }.time).replaceFirstChar { it.uppercase() }

    Scaffold(containerColor = bgColor) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Text("Agenda Médica", color = textColor, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text("Organización de consultas y procedimientos.", color = subtitleColor, fontSize = 14.sp)
            }

            // Toolbar: Toggle + Nueva Cita
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // View Toggle
                    Row(
                        modifier = Modifier
                            .border(1.dp, borderColor, RoundedCornerShape(8.dp))
                            .clip(RoundedCornerShape(8.dp))
                            .background(cardBg)
                    ) {
                        Box(
                            modifier = Modifier
                                .clickable { viewModel.onEvent(AppointmentEvent.SetViewMode(CalendarViewMode.WEEKLY)) }
                                .background(if (uiState.viewMode == CalendarViewMode.WEEKLY) primaryPink.copy(alpha = 0.1f) else Color.Transparent)
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Outlined.CalendarToday, contentDescription = "Vista Semanal",
                                tint = if (uiState.viewMode == CalendarViewMode.WEEKLY) primaryPink else subtitleColor,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        VerticalDivider(color = borderColor, modifier = Modifier.height(36.dp))
                        Box(
                            modifier = Modifier
                                .clickable { viewModel.onEvent(AppointmentEvent.SetViewMode(CalendarViewMode.MONTHLY)) }
                                .background(if (uiState.viewMode == CalendarViewMode.MONTHLY) primaryPink.copy(alpha = 0.1f) else Color.Transparent)
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Outlined.CalendarMonth, contentDescription = "Vista Mensual",
                                tint = if (uiState.viewMode == CalendarViewMode.MONTHLY) primaryPink else subtitleColor,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    // Nueva Cita Button
                    Button(
                        onClick = goToAddAppointment,
                        modifier = Modifier.height(40.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = primaryPink)
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Nueva Cita", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                    }
                }
            }

            // Calendar Card
            item {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = cardBg),
                    border = androidx.compose.foundation.BorderStroke(1.dp, borderColor),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        // Navigation Header
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Row(
                                    modifier = Modifier
                                        .border(1.dp, borderColor, RoundedCornerShape(8.dp))
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(cardBg)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .clickable { viewModel.onEvent(AppointmentEvent.PrevMonth) }
                                            .padding(horizontal = 8.dp, vertical = 6.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Prev", tint = subtitleColor, modifier = Modifier.size(18.dp))
                                    }
                                    VerticalDivider(color = borderColor, modifier = Modifier.height(30.dp))
                                    Box(
                                        modifier = Modifier
                                            .clickable { viewModel.onEvent(AppointmentEvent.NextMonth) }
                                            .padding(horizontal = 8.dp, vertical = 6.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Next", tint = subtitleColor, modifier = Modifier.size(18.dp))
                                    }
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(monthName, color = textColor, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                            }

                            TextButton(onClick = { viewModel.onEvent(AppointmentEvent.GoToToday) }) {
                                Text("Hoy", color = Color(0xFF2C5282), fontWeight = FontWeight.Medium)
                            }
                        }

                        HorizontalDivider(color = borderColor, thickness = 0.5.dp)

                        if (uiState.viewMode == CalendarViewMode.MONTHLY) {
                            MonthlyCalendarView(
                                uiState = uiState,
                                textColor = textColor,
                                subtitleColor = subtitleColor,
                                primaryPink = primaryPink,
                                borderColor = borderColor,
                                onDateClick = { viewModel.onEvent(AppointmentEvent.SelectDate(it)) }
                            )
                        } else {
                            WeeklyCalendarView(
                                uiState = uiState,
                                textColor = textColor,
                                subtitleColor = subtitleColor,
                                primaryPink = primaryPink,
                                borderColor = borderColor,
                                cardBg = cardBg,
                                onDateClick = { viewModel.onEvent(AppointmentEvent.SelectDate(it)) }
                            )
                        }

                        // Legend
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            LegendItem(color = Color(0xFF3B82F6), label = "Programada", textCol = subtitleColor)
                            LegendItem(color = primaryPink, label = "Confirmada", textCol = subtitleColor)
                            LegendItem(color = Color(0xFF10B981), label = "Completada", textCol = subtitleColor)
                            LegendItem(color = Color(0xFFEF4444), label = "Cancelada", textCol = subtitleColor)
                        }
                    }
                }
            }

            // Day Summary Card
            item {
                DaySummaryCard(
                    uiState = uiState,
                    textColor = textColor,
                    subtitleColor = subtitleColor,
                    primaryPink = primaryPink,
                    cardBg = cardBg,
                    borderColor = borderColor
                )
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

// ── Monthly Calendar Grid ──

@Composable
fun MonthlyCalendarView(
    uiState: AppointmentUiState,
    textColor: Color,
    subtitleColor: Color,
    primaryPink: Color,
    borderColor: Color,
    onDateClick: (String) -> Unit
) {
    val daysOfWeek = listOf("Dom", "Lun", "Mar", "Mié", "Jue", "Vie", "Sáb")
    val days = getDaysInMonth(uiState.currentYear, uiState.currentMonth)
    val todayStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 4.dp)) {
        // Weekday headers
        Row(modifier = Modifier.fillMaxWidth()) {
            daysOfWeek.forEach { day ->
                Text(
                    text = day, color = subtitleColor, fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold, textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f).padding(vertical = 8.dp)
                )
            }
        }

        // Grid rows
        val rows = days.chunked(7)
        rows.forEach { week ->
            Row(modifier = Modifier.fillMaxWidth()) {
                week.forEach { dayInfo ->
                    val dateStr = dayInfo.dateStr
                    val isCurrentMonth = dayInfo.isCurrentMonth
                    val isToday = dateStr == todayStr
                    val isSelected = dateStr == uiState.selectedDate
                    val hasAppt = uiState.appointments.any { it.startTime.startsWith(dateStr) }
                    val apptCount = uiState.appointments.count { it.startTime.startsWith(dateStr) }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(0.85f)
                            .border(0.5.dp, borderColor.copy(alpha = 0.3f))
                            .background(
                                when {
                                    isSelected -> primaryPink.copy(alpha = 0.05f)
                                    !isCurrentMonth -> Color.Transparent
                                    else -> Color.Transparent
                                }
                            )
                            .clickable { onDateClick(dateStr) },
                        contentAlignment = Alignment.TopCenter
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(top = 4.dp)
                        ) {
                            // Day number
                            Box(
                                modifier = Modifier
                                    .size(26.dp)
                                    .background(
                                        when {
                                            isToday -> primaryPink
                                            isSelected -> primaryPink.copy(alpha = 0.15f)
                                            else -> Color.Transparent
                                        },
                                        CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = dayInfo.dayNumber.toString(),
                                    color = when {
                                        isToday -> Color.White
                                        !isCurrentMonth -> subtitleColor.copy(alpha = 0.4f)
                                        isSelected -> primaryPink
                                        else -> textColor
                                    },
                                    fontSize = 12.sp,
                                    fontWeight = if (isToday || isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            }

                            // Appointment count badge
                            if (hasAppt && isCurrentMonth) {
                                Spacer(modifier = Modifier.height(2.dp))
                                Box(
                                    modifier = Modifier
                                        .background(Color(0xFFF1F5F9), RoundedCornerShape(8.dp))
                                        .padding(horizontal = 4.dp, vertical = 1.dp)
                                ) {
                                    Text(apptCount.toString(), fontSize = 9.sp, color = subtitleColor, fontWeight = FontWeight.Medium)
                                }
                            }

                            // Status dots
                            if (hasAppt && isCurrentMonth) {
                                Spacer(modifier = Modifier.height(2.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                                    val dayAppts = uiState.appointments.filter { it.startTime.startsWith(dateStr) }.take(3)
                                    dayAppts.forEach { appt ->
                                        Box(
                                            modifier = Modifier
                                                .size(5.dp)
                                                .background(getStatusColor(appt.status), CircleShape)
                                        )
                                    }
                                }
                            }
                        }

                        // Selected border
                        if (isSelected) {
                            Box(modifier = Modifier.matchParentSize().border(2.dp, primaryPink))
                        }
                    }
                }
            }
        }
    }
}

// ── Weekly / List Calendar View ──

@Composable
fun WeeklyCalendarView(
    uiState: AppointmentUiState,
    textColor: Color,
    subtitleColor: Color,
    primaryPink: Color,
    borderColor: Color,
    cardBg: Color,
    onDateClick: (String) -> Unit
) {
    // Show the current week (Mon-Sat) of the selected month
    val cal = Calendar.getInstance()
    cal.set(Calendar.YEAR, uiState.currentYear)
    cal.set(Calendar.MONTH, uiState.currentMonth)
    cal.set(Calendar.DAY_OF_MONTH, 1)

    // Generate all weeks of this month
    val allDays = getDaysInMonth(uiState.currentYear, uiState.currentMonth)
        .filter { it.isCurrentMonth }
    val weeks = allDays.chunked(7)

    // Find which week contains today or selected date
    val selectedDayNum = try {
        uiState.selectedDate.split("-").last().toInt()
    } catch (e: Exception) { 1 }

    val todayStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    val hours = (8..17).toList()

    Column(modifier = Modifier.fillMaxWidth()) {
        // Horizontal scrollable week day headers
        val scrollState = rememberScrollState()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollState)
                .padding(horizontal = 8.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            allDays.forEach { dayInfo ->
                val isToday = dayInfo.dateStr == todayStr
                val isSelected = dayInfo.dateStr == uiState.selectedDate
                val dayLabel = SimpleDateFormat("EEE", Locale("es", "ES")).format(
                    Calendar.getInstance().apply {
                        set(Calendar.YEAR, uiState.currentYear)
                        set(Calendar.MONTH, uiState.currentMonth)
                        set(Calendar.DAY_OF_MONTH, dayInfo.dayNumber)
                    }.time
                ).replaceFirstChar { it.uppercase() }

                Column(
                    modifier = Modifier
                        .width(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            when {
                                isSelected -> primaryPink
                                isToday -> primaryPink.copy(alpha = 0.1f)
                                else -> Color.Transparent
                            }
                        )
                        .clickable { onDateClick(dayInfo.dateStr) }
                        .padding(vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(dayLabel, fontSize = 11.sp, color = if (isSelected) Color.White.copy(alpha = 0.7f) else subtitleColor)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        dayInfo.dayNumber.toString(),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSelected) Color.White else textColor
                    )

                    // Dot for appointments
                    val hasAppts = uiState.appointments.any { it.startTime.startsWith(dayInfo.dateStr) }
                    if (hasAppts) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Box(modifier = Modifier.size(5.dp).background(if (isSelected) Color.White else primaryPink, CircleShape))
                    }
                }
            }
        }

        HorizontalDivider(color = borderColor, thickness = 0.5.dp)

        // Time slots for selected day
        val selectedDayAppts = uiState.appointmentsForSelectedDate.sortedBy { it.startTime }

        Column(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)) {
            hours.forEach { hour ->
                val hourStr = String.format(Locale.US, "%02d", hour)
                val apptsThisHour = selectedDayAppts.filter {
                    val apptHour = try { it.startTime.substring(11, 13) } catch (e: Exception) { "" }
                    apptHour == hourStr
                }

                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    // Time label
                    Text(
                        "${hour}:00", color = subtitleColor, fontSize = 11.sp,
                        modifier = Modifier.width(44.dp).padding(top = 4.dp)
                    )

                    // Divider + appointments
                    Column(modifier = Modifier.weight(1f)) {
                        HorizontalDivider(color = borderColor.copy(alpha = 0.3f), thickness = 0.5.dp)

                        if (apptsThisHour.isEmpty()) {
                            Spacer(modifier = Modifier.height(32.dp))
                        } else {
                            apptsThisHour.forEach { appt ->
                                Card(
                                    shape = RoundedCornerShape(8.dp),
                                    colors = CardDefaults.cardColors(containerColor = getStatusBgColor(appt.status)),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 2.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .width(3.dp)
                                                .height(32.dp)
                                                .background(getStatusColor(appt.status), RoundedCornerShape(2.dp))
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Column {
                                            Text(appt.patientName, color = textColor, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                            Text(appt.type, color = subtitleColor, fontSize = 11.sp)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ── Day Summary Card ──

@Composable
fun DaySummaryCard(
    uiState: AppointmentUiState,
    textColor: Color,
    subtitleColor: Color,
    primaryPink: Color,
    cardBg: Color,
    borderColor: Color
) {
    val selectedDateLabel = try {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outFmt = SimpleDateFormat("EEEE, d 'de' MMMM", Locale("es", "ES"))
        outFmt.format(sdf.parse(uiState.selectedDate)!!)
            .replaceFirstChar { it.uppercase() }
    } catch (e: Exception) { uiState.selectedDate }

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        border = androidx.compose.foundation.BorderStroke(1.dp, borderColor),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Resumen del Día", color = textColor, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(selectedDateLabel, color = subtitleColor, fontSize = 13.sp)
                }

                Box(
                    modifier = Modifier
                        .background(primaryPink.copy(alpha = 0.1f), RoundedCornerShape(16.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        "${uiState.appointmentsForSelectedDate.size} cita${if (uiState.appointmentsForSelectedDate.size != 1) "s" else ""}",
                        color = primaryPink, fontSize = 12.sp, fontWeight = FontWeight.Medium
                    )
                }
            }

            HorizontalDivider(color = borderColor, thickness = 1.dp)

            if (uiState.appointmentsForSelectedDate.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxWidth().height(150.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Outlined.Schedule, contentDescription = null, tint = Color(0xFFD1D5DB), modifier = Modifier.size(40.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("No hay citas programadas para este día.", color = subtitleColor, fontSize = 14.sp)
                    }
                }
            } else {
                Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                    uiState.appointmentsForSelectedDate
                        .sortedBy { it.startTime }
                        .forEach { appt ->
                            Card(
                                shape = RoundedCornerShape(10.dp),
                                colors = CardDefaults.cardColors(containerColor = getStatusBgColor(appt.status)),
                                border = androidx.compose.foundation.BorderStroke(1.dp, getStatusColor(appt.status).copy(alpha = 0.3f)),
                                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Outlined.Schedule, contentDescription = null, tint = subtitleColor, modifier = Modifier.size(14.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            val timeLabel = try { appt.startTime.substring(11, 16) } catch (e: Exception) { appt.startTime }
                                            Text(timeLabel, color = textColor, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                        }
                                        Box(
                                            modifier = Modifier
                                                .background(getStatusColor(appt.status).copy(alpha = 0.15f), RoundedCornerShape(12.dp))
                                                .padding(horizontal = 8.dp, vertical = 2.dp)
                                        ) {
                                            Text(getStatusLabel(appt.status), color = getStatusColor(appt.status), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(appt.patientName, color = textColor, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                                    Text(appt.type, color = subtitleColor, fontSize = 12.sp)
                                    if (!appt.notes.isNullOrBlank()) {
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text("📝 ${appt.notes}", color = subtitleColor, fontSize = 11.sp)
                                    }
                                }
                            }
                        }
                }
            }
        }
    }
}

// ── Helpers ──

data class DayInfo(val dayNumber: Int, val dateStr: String, val isCurrentMonth: Boolean)

fun getDaysInMonth(year: Int, month: Int): List<DayInfo> {
    val cal = Calendar.getInstance()
    cal.set(Calendar.YEAR, year)
    cal.set(Calendar.MONTH, month)
    cal.set(Calendar.DAY_OF_MONTH, 1)

    val startingDayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1 // 0=Sun
    val totalDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH)

    val result = mutableListOf<DayInfo>()

    // Previous month days
    val prevCal = Calendar.getInstance()
    prevCal.set(Calendar.YEAR, year)
    prevCal.set(Calendar.MONTH, month)
    prevCal.add(Calendar.MONTH, -1)
    val prevMonthDays = prevCal.getActualMaximum(Calendar.DAY_OF_MONTH)

    for (i in 0 until startingDayOfWeek) {
        val day = prevMonthDays - startingDayOfWeek + 1 + i
        val prevMonth = if (month == 0) 11 else month - 1
        val prevYear = if (month == 0) year - 1 else year
        val dateStr = String.format(Locale.US, "%04d-%02d-%02d", prevYear, prevMonth + 1, day)
        result.add(DayInfo(day, dateStr, false))
    }

    // Current month days
    for (i in 1..totalDays) {
        val dateStr = String.format(Locale.US, "%04d-%02d-%02d", year, month + 1, i)
        result.add(DayInfo(i, dateStr, true))
    }

    // Next month days
    val totalSlots = result.size
    val remaining = (7 - (totalSlots % 7)) % 7
    for (i in 1..remaining) {
        val nextMonth = if (month == 11) 0 else month + 1
        val nextYear = if (month == 11) year + 1 else year
        val dateStr = String.format(Locale.US, "%04d-%02d-%02d", nextYear, nextMonth + 1, i)
        result.add(DayInfo(i, dateStr, false))
    }

    return result
}

fun getStatusColor(status: String): Color {
    return when (status.lowercase()) {
        "completed" -> Color(0xFF10B981)
        "cancelled" -> Color(0xFFEF4444)
        "confirmed" -> Color(0xFFEC5C76)
        "no_show" -> Color(0xFFF59E0B)
        else -> Color(0xFF3B82F6) // scheduled
    }
}

fun getStatusBgColor(status: String): Color {
    return when (status.lowercase()) {
        "completed" -> Color(0xFFECFDF5)
        "cancelled" -> Color(0xFFFEF2F2)
        "confirmed" -> Color(0xFFFFF1F2)
        else -> Color(0xFFEFF6FF)
    }
}

fun getStatusLabel(status: String): String {
    return when (status.lowercase()) {
        "completed" -> "Completada"
        "cancelled" -> "Cancelada"
        "confirmed" -> "Confirmada"
        "no_show" -> "No Asistió"
        "scheduled" -> "Programada"
        else -> status
    }
}

@Composable
fun LegendItem(color: Color, label: String, textCol: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(8.dp).background(color, CircleShape))
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = label, color = textCol, fontSize = 10.sp)
    }
}
