package com.focusmate.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.focusmate.data.model.Event
import com.focusmate.data.model.Habit
import com.focusmate.data.model.HabitCompletion
import com.focusmate.ui.viewModel.HabitViewModel
import com.focusmate.ui.viewModel.StatisticsViewModel
import com.focusmate.ui.viewModel.EventViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(
    navController: NavHostController,
    statisticsViewModel: StatisticsViewModel = hiltViewModel(),
    habitViewModel: HabitViewModel = hiltViewModel(),
    eventViewModel: EventViewModel = hiltViewModel()
) {
    val habitCompletions by statisticsViewModel.habitCompletions.observeAsState(emptyList())
    val totalHabitsCompleted by statisticsViewModel.totalHabitsCompleted.observeAsState(0)
    val totalEventsCompleted by statisticsViewModel.totalEventsCompleted.observeAsState(0)
    val habits by habitViewModel.allHabits.observeAsState(emptyList())
    val events by eventViewModel.allEvents.observeAsState(emptyList())
    val selectedTab = remember { mutableStateOf("Habits") }
    val selectedStatOption = remember { mutableStateOf("Frecuencia") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Estadísticas") },
            )
        }
    ) { innerPadding ->
        StatisticsContent(
            modifier = Modifier.padding(innerPadding),
            habitCompletions = habitCompletions,
            totalHabitsCompleted = totalHabitsCompleted,
            totalEventsCompleted = totalEventsCompleted,
            habits = habits,
            events = events,
            selectedTab = selectedTab.value,
            onTabSelected = { selectedTab.value = it },
            selectedStatOption = selectedStatOption.value,
            onStatOptionSelected = { selectedStatOption.value = it }
        )
    }
}

@Composable
fun StatisticsContent(
    modifier: Modifier = Modifier,
    habitCompletions: List<HabitCompletion>,
    totalHabitsCompleted: Int,
    totalEventsCompleted: Int,
    habits: List<Habit>,
    events: List<Event>,
    selectedTab: String,
    onTabSelected: (String) -> Unit,
    selectedStatOption: String,
    onStatOptionSelected: (String) -> Unit
) {
    val totalHabits = 20
    val totalEvents = 10

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Card(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
                    .clickable { onTabSelected("Habits") },
                colors = if (selectedTab == "Habits") CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary) else CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Hábitos", style = MaterialTheme.typography.headlineSmall)
                    Text("$totalHabitsCompleted/$totalHabits", style = MaterialTheme.typography.displayMedium)
                }
            }

            Card(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
                    .clickable { onTabSelected("Events") },
                colors = if (selectedTab == "Events") CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary) else CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Eventos", style = MaterialTheme.typography.headlineSmall)
                    Text("$totalEventsCompleted/$totalEvents", style = MaterialTheme.typography.displayMedium)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (selectedTab == "Habits") {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                listOf("Frecuencia", "Categoría").forEach { option ->
                    Text(
                        text = option,
                        modifier = Modifier.clickable { onStatOptionSelected(option) },
                        fontWeight = if (selectedStatOption == option) FontWeight.Bold else FontWeight.Normal,
                        color = if (selectedStatOption == option) Color.Magenta else Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            when (selectedStatOption) {
                "Frecuencia" -> FrequencyStatistics(habitCompletions, habits)
                "Categoría" -> CategoryStatistics(habitCompletions, habits)
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                listOf("Frecuencia", "Categoría").forEach { option ->
                    Text(
                        text = option,
                        modifier = Modifier.clickable { onStatOptionSelected(option) },
                        fontWeight = if (selectedStatOption == option) FontWeight.Bold else FontWeight.Normal,
                        color = if (selectedStatOption == option) Color.Magenta else Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            when (selectedStatOption) {
                "Frecuencia" -> FrequencyStatisticsEvents(events)
                "Categoría" -> CategoryStatisticsEvents(events)
            }
        }
    }
}

@Composable
fun getCategoryColor(category: String): Color {
    return when (category) {
        "Trabajo" -> Color(0xFF1E88E5)
        "Personal" -> Color(0xFFD32F2F)
        "Salud" -> Color(0xFF388E3C)
        "Finanzas" -> Color(0xFFFBC02D)
        "Estudio" -> Color(0xFF8E24AA)
        "Ocio" -> Color(0xFFFF9800)
        "Otro" -> Color(0xFF757575)
        else -> Color.Gray
    }
}

@Composable
fun FrequencyStatistics(habitCompletions: List<HabitCompletion>, habits: List<Habit>) {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val now = Date()
    val calendar = Calendar.getInstance()

    val lastDayCompletions = habitCompletions.filter { it.completionDate >= (now.time - 86400000) }
    val lastWeekCompletions = habitCompletions.filter { it.completionDate >= (now.time - 604800000) }
    val lastMonthCompletions = habitCompletions.filter {
        calendar.time = now
        val currentMonth = calendar.get(Calendar.MONTH)
        calendar.time = Date(it.completionDate)
        calendar.get(Calendar.MONTH) == currentMonth
    }

    Column {
        Text("Frecuencia de Hábitos Completados", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Card(
                modifier = Modifier
                    .padding(8.dp)
                    .width(100.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Último día", style = MaterialTheme.typography.bodyLarge)
                    Text("${lastDayCompletions.size}", style = MaterialTheme.typography.headlineMedium)
                }
            }
            Card(
                modifier = Modifier
                    .padding(8.dp)
                    .width(100.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Última semana", style = MaterialTheme.typography.bodyLarge)
                    Text("${lastWeekCompletions.size}", style = MaterialTheme.typography.headlineMedium)
                }
            }
            Card(
                modifier = Modifier
                    .padding(8.dp)
                    .width(100.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Último mes", style = MaterialTheme.typography.bodyLarge)
                    Text("${lastMonthCompletions.size}", style = MaterialTheme.typography.headlineMedium)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(habitCompletions) { completion ->
                val habit = habits.find { it.id == completion.habitId }
                Card(
                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxWidth()
                ) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            habit?.let {
                                Text("Título: ${it.title}", style = MaterialTheme.typography.bodyLarge)
                                Text("Fecha: ${dateFormat.format(Date(completion.completionDate))}", style = MaterialTheme.typography.bodyLarge)
                            } ?: Text("Hábito no encontrado", style = MaterialTheme.typography.bodyLarge)
                        }
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .background(getCategoryColor(habit?.tag ?: "Otro"), shape = CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(habit?.tag ?: "Otro", color = Color.White, style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryStatistics(habitCompletions: List<HabitCompletion>, habits: List<Habit>) {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val habitsByCategory = habits.groupBy { it.tag }

    Column {
        Text("Categoría de Hábitos Completados", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(16.dp))

        LazyRow(modifier = Modifier.fillMaxWidth()) {
            habitsByCategory.forEach { (category, habits) ->
                val completedHabitsInCategory = habits.filter { habit ->
                    habitCompletions.any { it.habitId == habit.id }
                }
                item {
                    Card(
                        modifier = Modifier
                            .padding(8.dp)
                            .width(100.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(category, style = MaterialTheme.typography.bodyLarge)
                            Text("${completedHabitsInCategory.size}", style = MaterialTheme.typography.headlineMedium)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(habitCompletions) { completion ->
                val habit = habits.find { it.id == completion.habitId }
                Card(
                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxWidth()
                ) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            habit?.let {
                                Text("Título: ${it.title}", style = MaterialTheme.typography.bodyLarge)
                                Text("Fecha: ${dateFormat.format(Date(completion.completionDate))}", style = MaterialTheme.typography.bodyLarge)
                            } ?: Text("Hábito no encontrado", style = MaterialTheme.typography.bodyLarge)
                        }
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .background(getCategoryColor(habit?.tag ?: "Otro"), shape = CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(habit?.tag ?: "Otro", color = Color.White, style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FrequencyStatisticsEvents(events: List<Event>) {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val now = Date()
    val calendar = Calendar.getInstance()

    val lastDayCompletions = events.filter { it.isCompleted && it.date?.time ?: 0 >= (now.time - 86400000) }
    val lastWeekCompletions = events.filter { it.isCompleted && it.date?.time ?: 0 >= (now.time - 604800000) }
    val lastMonthCompletions = events.filter {
        calendar.time = now
        val currentMonth = calendar.get(Calendar.MONTH)
        calendar.time = it.date ?: Date(0)
        calendar.get(Calendar.MONTH) == currentMonth
    }

    Column {
        Text("Frecuencia de Eventos Completados", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Card(
                modifier = Modifier
                    .padding(8.dp)
                    .width(100.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Último día", style = MaterialTheme.typography.bodyLarge)
                    Text("${lastDayCompletions.size}", style = MaterialTheme.typography.headlineMedium)
                }
            }
            Card(
                modifier = Modifier
                    .padding(8.dp)
                    .width(100.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Última semana", style = MaterialTheme.typography.bodyLarge)
                    Text("${lastWeekCompletions.size}", style = MaterialTheme.typography.headlineMedium)
                }
            }
            Card(
                modifier = Modifier
                    .padding(8.dp)
                    .width(100.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Último mes", style = MaterialTheme.typography.bodyLarge)
                    Text("${lastMonthCompletions.size}", style = MaterialTheme.typography.headlineMedium)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(events.filter { it.isCompleted }) { event ->
                Card(
                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxWidth()
                ) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Título: ${event.title}", style = MaterialTheme.typography.bodyLarge)
                            Text("Fecha: ${event.date?.let { dateFormat.format(it) } ?: "Sin fecha"}", style = MaterialTheme.typography.bodyLarge)
                        }
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .background(getCategoryColor(event.tag.toString()), shape = CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            event.tag?.let { Text(it, color = Color.White, style = MaterialTheme.typography.bodyLarge) }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryStatisticsEvents(events: List<Event>) {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val eventsByCategory = events.groupBy { it.tag }

    Column {
        Text("Categoría de Eventos Completados", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(16.dp))

        LazyRow(modifier = Modifier.fillMaxWidth()) {
            eventsByCategory.forEach { (tag, events) ->
                val completedEventsInCategory = events.filter { it.isCompleted }
                item {
                    Card(
                        modifier = Modifier
                            .padding(8.dp)
                            .width(100.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            if (tag != null) {
                                Text(tag, style = MaterialTheme.typography.bodyLarge)
                            }
                            Text("${completedEventsInCategory.size}", style = MaterialTheme.typography.headlineMedium)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(events.filter { it.isCompleted }) { event ->
                Card(
                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxWidth()
                ) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Título: ${event.title}", style = MaterialTheme.typography.bodyLarge)
                            Text("Fecha: ${event.date?.let { dateFormat.format(it) } ?: "Sin fecha"}", style = MaterialTheme.typography.bodyLarge)
                        }
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .background(getCategoryColor(event.tag.toString()), shape = CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            event.tag?.let { Text(it, color = Color.White, style = MaterialTheme.typography.bodyLarge) }
                        }
                    }
                }
            }
        }
    }
}



