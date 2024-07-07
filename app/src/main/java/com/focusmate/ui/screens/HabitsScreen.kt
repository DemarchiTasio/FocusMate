package com.focusmate.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.focusmate.data.model.Habit
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitsScreen(navController: NavHostController) {
    var showDialog by remember { mutableStateOf(false) }
    var habits by remember { mutableStateOf(listOf<Habit>()) }
    var selectedTab by remember { mutableStateOf("Hoy") }

    Scaffold(
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                Text("Hábitos", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(16.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Hoy",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = if (selectedTab == "Hoy") Color.White else Color.Gray,
                        modifier = Modifier
                            .padding(16.dp)
                            .clickable { selectedTab = "Hoy" }
                    )
                    Text(
                        text = "Semanal",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = if (selectedTab == "Semanal") Color.White else Color.Gray,
                        modifier = Modifier
                            .padding(16.dp)
                            .clickable { selectedTab = "Semanal" }
                    )
                    Text(
                        text = "General",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = if (selectedTab == "General") Color.White else Color.Gray,
                        modifier = Modifier
                            .padding(16.dp)
                            .clickable { selectedTab = "General" }
                    )
                }

                Box(modifier = Modifier.weight(1f)) {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(habits) { habit ->
                            HabitItem(
                                habit = habit,
                                onMarkCompleted = {
                                    habits = habits.map {
                                        if (it.id == habit.id) it.copy(isCompleted = true) else it
                                    }
                                },
                                onMarkNotCompleted = {
                                    habits = habits.map {
                                        if (it.id == habit.id) it.copy(isCompleted = false) else it
                                    }
                                }
                            )
                        }
                    }
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog = true },
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Habit")
            }
        }
    )

    if (showDialog) {
        AddHabitDialog(
            onAddHabit = { habit ->
                habits = habits + habit
                showDialog = false
            },
            onDismiss = { showDialog = false }
        )
    }
}

@Composable
fun AddHabitDialog(
    onAddHabit: (Habit) -> Unit,
    onDismiss: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var frequency by remember { mutableStateOf("Diario") }
    var selectedDays by remember { mutableStateOf(listOf<Int>()) }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Agregar Hábito") },
        text = {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .heightIn(max = 400.dp)
            ) {
                TextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Título") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Frecuencia", style = MaterialTheme.typography.bodyLarge)
                Row {
                    listOf("Diario", "Semanal", "Mensual").forEach { freq ->
                        Text(
                            text = freq,
                            color = if (frequency == freq) MaterialTheme.colorScheme.primary else Color.Gray,
                            modifier = Modifier
                                .padding(8.dp)
                                .clickable { frequency = freq },
                            fontWeight = if (frequency == freq) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                if (frequency == "Diario") {
                    Text(text = "Días de la semana", style = MaterialTheme.typography.bodyLarge)
                    Row {
                        listOf("L", "M", "X", "J", "V", "S", "D").forEachIndexed { index, day ->
                            Text(
                                text = day,
                                color = if (selectedDays.contains(index + 1)) MaterialTheme.colorScheme.primary else Color.Gray,
                                modifier = Modifier
                                    .padding(8.dp)
                                    .clickable {
                                        if (selectedDays.contains(index + 1)) {
                                            selectedDays = selectedDays - (index + 1)
                                        } else {
                                            selectedDays = selectedDays + (index + 1)
                                        }
                                    },
                                fontWeight = if (selectedDays.contains(index + 1)) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                } else if (frequency == "Semanal") {
                    Text(text = "Días a la semana", style = MaterialTheme.typography.bodyLarge)
                    Row {
                        (1..7).forEach { day ->
                            Text(
                                text = day.toString(),
                                color = if (selectedDays.contains(day)) MaterialTheme.colorScheme.primary else Color.Gray,
                                modifier = Modifier
                                    .padding(8.dp)
                                    .clickable {
                                        if (selectedDays.contains(day)) {
                                            selectedDays = selectedDays - day
                                        } else {
                                            selectedDays = selectedDays + day
                                        }
                                    },
                                fontWeight = if (selectedDays.contains(day)) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                } else if (frequency == "Mensual") {
                    Text(text = "Días del mes", style = MaterialTheme.typography.bodyLarge)
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(4),
                        contentPadding = PaddingValues(8.dp),
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        items((1..31).toList()) { day ->
                            Text(
                                text = day.toString(),
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = if (selectedDays.contains(day)) MaterialTheme.colorScheme.primary else Color.Gray,
                                modifier = Modifier
                                    .padding(8.dp)
                                    .background(if (selectedDays.contains(day)) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f) else Color.Transparent)
                                    .clickable {
                                        if (selectedDays.contains(day)) {
                                            selectedDays = selectedDays - day
                                        } else {
                                            selectedDays = selectedDays + day
                                        }
                                    }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                if (title.isNotEmpty()) {
                    onAddHabit(Habit(title = title, frequency = when (frequency) {
                        "Diario" -> 1
                        "Semanal" -> 2
                        "Mensual" -> 3
                        else -> 1
                    }, isCompleted = false))
                    onDismiss()
                }
            }) {
                Text("Agregar")
            }
        },
        dismissButton = {
            Button(onClick = { onDismiss() }) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
fun HabitItem(
    habit: Habit,
    onMarkCompleted: () -> Unit,
    onMarkNotCompleted: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { /* Handle click event */ },
        colors = CardDefaults.cardColors(
            containerColor = if (habit.isCompleted) Color.Gray else Color(0xFF4CAF50)
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = habit.title,
                    color = Color.White,
                    fontSize = 18.sp
                )
                if (habit.isCompleted) {
                    Text(
                        text = "Completado",
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }
            }
            Row {
                IconButton(onClick = onMarkCompleted) {
                    Icon(Icons.Default.Check, contentDescription = "Marcar como completado")
                }
                IconButton(onClick = onMarkNotCompleted) {
                    Icon(Icons.Default.Clear, contentDescription = "Marcar como no completado")
                }
            }
        }
    }
}