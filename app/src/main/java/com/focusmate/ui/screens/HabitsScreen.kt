package com.focusmate.ui.screens

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.focusmate.data.model.Habit
import com.focusmate.ui.viewModel.HabitViewModel
import java.text.SimpleDateFormat
import java.util.*

val habitTags = listOf("Trabajo", "Personal", "Salud", "Finanzas", "Estudio", "Ocio", "Otro")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitsScreen(
    navController: NavHostController,
    habitViewModel: HabitViewModel = hiltViewModel()
) {
    var showDialog by remember { mutableStateOf(false) }
    var habitToEdit by remember { mutableStateOf<Habit?>(null) }
    var selectedFrequency by remember { mutableStateOf("Hoy") }
    val habits by habitViewModel.allHabits.observeAsState(emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Hábitos") },
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { habitToEdit = null; showDialog = true },
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Habit")
            }
        }
    ) { innerPadding ->
        HabitsContent(
            modifier = Modifier.padding(innerPadding),
            showDialog = showDialog,
            onDismiss = { showDialog = false },
            onShowDialog = { showDialog = true },
            onEditHabit = { habitToEdit = it; showDialog = true },
            habitToEdit = habitToEdit,
            selectedFrequency = selectedFrequency,
            onFrequencyChange = { selectedFrequency = it },
            habits = habits,  // Pasar la lista de hábitos
            onAddHabit = { habitViewModel.insert(it); showDialog = false },
            onUpdateHabit = { habitViewModel.update(it); showDialog = false },
            onDeleteHabit = { habitViewModel.delete(it); showDialog = false },
            habitViewModel = habitViewModel
        )
    }
}


@Composable
fun HabitsContent(
    modifier: Modifier = Modifier,
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onShowDialog: () -> Unit,
    onEditHabit: (Habit) -> Unit,
    habitToEdit: Habit?,
    selectedFrequency: String,
    onFrequencyChange: (String) -> Unit,
    habits: List<Habit>,
    onAddHabit: (Habit) -> Unit,
    onUpdateHabit: (Habit) -> Unit,
    onDeleteHabit: (Habit) -> Unit,
    habitViewModel: HabitViewModel
) {
    val context = LocalContext.current
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val currentDayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)

    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        Text("Frecuencia", style = MaterialTheme.typography.headlineSmall)

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            listOf("Hoy", "Semanal", "General").forEach { frequency ->
                Text(
                    text = frequency,
                    modifier = Modifier.clickable { onFrequencyChange(frequency) },
                    fontWeight = if (selectedFrequency == frequency) FontWeight.Bold else FontWeight.Normal,
                    color = if (selectedFrequency == frequency) Color.Magenta else Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            val filteredHabits = habits.filter { habit ->
                when (selectedFrequency) {
                    "Hoy" -> {
                        habit.frequency == "Diario" && habit.daysOfWeek.contains(currentDayOfWeek) ||
                                habit.frequency == "Semanal" && habit.weeklyCount > 0
                    }
                    "Semanal" -> {
                        habit.frequency == "Semanal" || habit.frequency == "Diario" && habit.daysOfWeek.any { it in 2..6 }
                    }
                    "General" -> true
                    else -> false
                }
            }
            items(filteredHabits) { habit ->
                HabitItem(
                    habit = habit,
                    onEditHabit = onEditHabit,
                    onCompleteHabit = {
                        onUpdateHabit(habit.copy(isCompleted = !habit.isCompleted))
                        if (!habit.isCompleted) {
                            habitViewModel.insertHabitCompletion(habit.id)
                        } else {
                            habitViewModel.removeHabitCompletion(habit.id)
                        }
                    }
                )
            }
        }

        if (showDialog) {
            AddEditHabitDialog(
                habit = habitToEdit,
                onAddHabit = onAddHabit,
                onUpdateHabit = onUpdateHabit,
                onDeleteHabit = onDeleteHabit,
                onDismiss = onDismiss
            )
        }
    }
}

@Composable
fun HabitItem(
    habit: Habit,
    onEditHabit: (Habit) -> Unit,
    onCompleteHabit: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onCompleteHabit() },
        colors = CardDefaults.cardColors(
            containerColor = if (habit.isCompleted) Color(0xFFA5D6A7) else Color.Gray
        )
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f).padding(16.dp)) {
                Text(habit.title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Text(
                    if (habit.isCompleted) "Completado" else "",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.White
                )
            }
            IconButton(onClick = { onEditHabit(habit) }) {
                Icon(Icons.Default.Edit, contentDescription = "Editar Hábito", tint = Color.White)
            }
        }
    }
}

@Composable
fun AddEditHabitDialog(
    habit: Habit?,
    onAddHabit: (Habit) -> Unit,
    onUpdateHabit: (Habit) -> Unit,
    onDeleteHabit: (Habit) -> Unit,
    onDismiss: () -> Unit
) {
    var title by remember { mutableStateOf(habit?.title ?: "") }
    var frequency by remember { mutableStateOf(habit?.frequency ?: "Diario") }
    var daysOfWeek by remember { mutableStateOf(habit?.daysOfWeek ?: emptyList()) }
    var weeklyCount by remember { mutableStateOf(habit?.weeklyCount ?: 1) }
    var selectedTag by remember { mutableStateOf(habit?.tag ?: habitTags[0]) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (habit != null) "Editar Hábito" else "Agregar Hábito") },
        text = {
            Column {
                if (habit != null) {
                    IconButton(
                        onClick = { onDeleteHabit(habit); onDismiss() },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Eliminar Hábito", tint = Color.Red)
                    }
                }
                TextField(value = title, onValueChange = { title = it }, label = { Text("Título") })

                Spacer(modifier = Modifier.height(8.dp))
                // Menú desplegable para etiquetas
                Text("Etiqueta", style = MaterialTheme.typography.bodyLarge)
                var expanded by remember { mutableStateOf(false) }
                Box(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = selectedTag,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { expanded = true }
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(16.dp)
                    )
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        habitTags.forEach { tag ->
                            DropdownMenuItem(
                                onClick = {
                                    selectedTag = tag
                                    expanded = false
                                },
                                text = { Text(text = tag) }
                            )
                        }
                    }
                }

                Text("Frecuencia", style = MaterialTheme.typography.bodyLarge)

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    listOf("Diario", "Semanal").forEach { freq ->
                        Text(
                            text = freq,
                            modifier = Modifier.clickable { frequency = freq },
                            fontWeight = if (frequency == freq) FontWeight.Bold else FontWeight.Normal,
                            color = if (frequency == freq) Color.Magenta else Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                when (frequency) {
                    "Diario" -> {
                        Text("Días de la semana", style = MaterialTheme.typography.bodyLarge)
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                            (1..7).forEach { day ->
                                Text(
                                    text = listOf("L", "M", "X", "J", "V", "S", "D")[day - 1],
                                    fontWeight = if (daysOfWeek.contains(day)) FontWeight.Bold else FontWeight.Normal,
                                    color = if (daysOfWeek.contains(day)) Color.Magenta else Color.White,
                                    modifier = Modifier.clickable {
                                        daysOfWeek = if (daysOfWeek.contains(day)) {
                                            daysOfWeek - day
                                        } else {
                                            daysOfWeek + day
                                        }
                                    }
                                )
                            }
                        }
                    }
                    "Semanal" -> {
                        Text("Número de días a la semana", style = MaterialTheme.typography.bodyLarge)
                        Slider(
                            value = weeklyCount.toFloat(),
                            onValueChange = { weeklyCount = it.toInt() },
                            valueRange = 1f..7f,
                            steps = 6
                        )
                        Text("$weeklyCount días a la semana", style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                val newHabit = Habit(
                    id = habit?.id ?: UUID.randomUUID().hashCode(),
                    title = title,
                    frequency = frequency,
                    daysOfWeek = daysOfWeek,
                    weeklyCount = weeklyCount,
                    isCompleted = false,
                    tag = selectedTag
                )
                if (habit != null) {
                    onUpdateHabit(newHabit)
                } else {
                    onAddHabit(newHabit)
                }
                onDismiss()
            }) {
                Text(if (habit != null) "Actualizar" else "Agregar")
            }
        },
        dismissButton = {
            Button(onClick = { onDismiss() }) {
                Text("Cancelar")
            }
        }
    )
}

