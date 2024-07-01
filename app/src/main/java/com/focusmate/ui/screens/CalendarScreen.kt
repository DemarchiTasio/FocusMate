package com.focusmate.ui.screens

import android.app.DatePickerDialog
import android.widget.DatePicker
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.focusmate.data.model.Event
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(navController: NavHostController) {
    var showDialog by remember { mutableStateOf(false) }
    var eventToEdit by remember { mutableStateOf<Event?>(null) }
    var selectedDate by remember { mutableStateOf<Date?>(null) }
    var currentMonth by remember { mutableStateOf(Calendar.getInstance().get(Calendar.MONTH)) }
    var currentYear by remember { mutableStateOf(Calendar.getInstance().get(Calendar.YEAR)) }
    var showNoDateEvents by remember { mutableStateOf(false) }
    var showCompleted by remember { mutableStateOf(false) }

    Scaffold(
        content = { innerPadding ->
            CalendarContent(
                modifier = Modifier.padding(innerPadding),
                showDialog = showDialog,
                onDismiss = { showDialog = false },
                onShowDialog = { showDialog = true },
                onEditEvent = { eventToEdit = it; showDialog = true },
                eventToEdit = eventToEdit,
                selectedDate = selectedDate,
                onDateSelected = { selectedDate = it },
                currentMonth = currentMonth,
                currentYear = currentYear,
                onMonthChange = { month, year ->
                    currentMonth = month
                    currentYear = year
                },
                showNoDateEvents = showNoDateEvents,
                onShowNoDateEventsChange = { showNoDateEvents = it },
                showCompleted = showCompleted,
                onShowCompletedChange = { showCompleted = it }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { eventToEdit = null; showDialog = true },
                shape = CircleShape // Redondear el botón flotante
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Event")
            }
        }
    )
}

@Composable
fun CalendarContent(
    modifier: Modifier = Modifier,
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onShowDialog: () -> Unit,
    onEditEvent: (Event) -> Unit,
    eventToEdit: Event?,
    selectedDate: Date?,
    onDateSelected: (Date?) -> Unit,
    currentMonth: Int,
    currentYear: Int,
    onMonthChange: (Int, Int) -> Unit,
    showNoDateEvents: Boolean,
    onShowNoDateEventsChange: (Boolean) -> Unit,
    showCompleted: Boolean,
    onShowCompletedChange: (Boolean) -> Unit
) {
    val context = LocalContext.current
    var events by remember { mutableStateOf(listOf<Event>()) }
    var completedEvents by remember { mutableStateOf(listOf<Event>()) }
    val noDateEvents = events.filter { it.date == null }
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Calendario", style = MaterialTheme.typography.headlineMedium)

        CalendarView(
            events = events,
            selectedDate = selectedDate,
            onDateSelected = {
                onDateSelected(it)
                onShowNoDateEventsChange(false)
                onShowCompletedChange(false)
            },
            currentMonth = currentMonth,
            currentYear = currentYear,
            onMonthChange = onMonthChange
        )

        Text("Eventos", style = MaterialTheme.typography.headlineSmall)

        Box(modifier = Modifier.weight(1f)) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                if (selectedDate != null) {
                    item {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                "Día: ${dateFormat.format(selectedDate)}",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                    items(events.filter {
                        it.date != null && dateFormat.format(it.date) == dateFormat.format(
                            selectedDate
                        )
                    }) { event ->
                        EventItem(
                            event = event,
                            isCompleted = false,
                            onEditEvent = onEditEvent,
                            onDeleteEvent = {
                                events = events.filter { it.id != event.id }
                                Toast.makeText(context, "Evento eliminado", Toast.LENGTH_SHORT).show()
                            },
                            onCompleteEvent = {
                                events = events.filter { it.id != event.id }
                                completedEvents = completedEvents + event
                                Toast.makeText(context, "Evento completado", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                } else if (showNoDateEvents) {
                    item {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Sin fecha", style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                    items(noDateEvents) { event ->
                        EventItem(
                            event = event,
                            isCompleted = false,
                            onEditEvent = onEditEvent,
                            onDeleteEvent = {
                                events = events.filter { it.id != event.id }
                                Toast.makeText(context, "Evento eliminado", Toast.LENGTH_SHORT).show()
                            },
                            onCompleteEvent = {
                                events = events.filter { it.id != event.id }
                                completedEvents = completedEvents + event
                                Toast.makeText(context, "Evento completado", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                } else if (showCompleted) {
                    item {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Completados", style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                    items(completedEvents.sortedByDescending { it.date }) { event ->
                        EventItem(
                            event = event,
                            isCompleted = true,
                            onEditEvent = onEditEvent,
                            onDeleteEvent = {
                                completedEvents = completedEvents.filter { it.id != event.id }
                                Toast.makeText(context, "Evento eliminado", Toast.LENGTH_SHORT).show()
                            },
                            onCompleteEvent = {
                                completedEvents = completedEvents.filter { it.id != event.id }
                                events = events + event
                                Toast.makeText(context, "Evento no completado", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                } else {
                    item {
                        Text("Todos", style = MaterialTheme.typography.bodyLarge)
                    }
                    items(events.sortedByDescending { it.date }) { event ->
                        EventItem(
                            event = event,
                            isCompleted = false,
                            onEditEvent = onEditEvent,
                            onDeleteEvent = {
                                events = events.filter { it.id != event.id }
                                Toast.makeText(context, "Evento eliminado", Toast.LENGTH_SHORT).show()
                            },
                            onCompleteEvent = {
                                events = events.filter { it.id != event.id }
                                completedEvents = completedEvents + event
                                Toast.makeText(context, "Evento completado", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                }
            }
        }

        // Column for fixed titles at the bottom
        Column {
            if (selectedDate == null && !showNoDateEvents && !showCompleted) {
                Text(
                    text = "Sin fecha",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onShowNoDateEventsChange(true)
                            onShowCompletedChange(false)
                        }
                        .padding(vertical = 8.dp)
                )
                Text(
                    text = "Completados",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onShowNoDateEventsChange(false)
                            onShowCompletedChange(true)
                        }
                        .padding(vertical = 8.dp)
                )
            } else {
                if (showNoDateEvents || showCompleted || selectedDate != null) {
                    Text(
                        text = "Todos",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onShowNoDateEventsChange(false)
                                onShowCompletedChange(false)
                                onDateSelected(null)
                            }
                            .padding(vertical = 8.dp)
                    )
                }
                if (showCompleted || selectedDate != null) {
                    Text(
                        text = "Sin fecha",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onShowNoDateEventsChange(true)
                                onShowCompletedChange(false)
                                onDateSelected(null)
                            }
                            .padding(vertical = 8.dp)
                    )
                }
                if (showNoDateEvents || selectedDate != null) {
                    Text(
                        text = "Completados",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onShowNoDateEventsChange(false)
                                onShowCompletedChange(true)
                                onDateSelected(null)
                            }
                            .padding(vertical = 8.dp)
                    )
                }
            }
        }

        if (showDialog) {
            AddEventDialog(
                event = eventToEdit,
                onAddEvent = { event ->
                    events = if (eventToEdit != null) {
                        events.map { if (it.id == event.id) event else it }
                    } else {
                        events + event
                    }
                    onDismiss()
                    Toast.makeText(
                        context,
                        "Evento ${if (eventToEdit != null) "actualizado" else "agregado"}",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                onDismiss = onDismiss,
                isEditable = eventToEdit?.let { !completedEvents.contains(it) } ?: true
            )
        }
    }
}


@Composable
fun CalendarView(
    events: List<Event>,
    selectedDate: Date?,
    onDateSelected: (Date) -> Unit,
    currentMonth: Int,
    currentYear: Int,
    onMonthChange: (Int, Int) -> Unit
) {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.MONTH, currentMonth)
    calendar.set(Calendar.YEAR, currentYear)
    calendar.set(Calendar.DAY_OF_MONTH, 1)
    val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    val firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val daysOfWeek = listOf("Dom", "Lun", "Mar", "Mié", "Jue", "Vie", "Sáb")
    val today = Calendar.getInstance().time

    Column {
        // Navegación entre meses
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                if (currentMonth == 0) {
                    onMonthChange(11, currentYear - 1)
                } else {
                    onMonthChange(currentMonth - 1, currentYear)
                }
            }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Mes anterior")
            }
            Text(
                text = "${
                    calendar.getDisplayName(
                        Calendar.MONTH,
                        Calendar.LONG,
                        Locale.getDefault()
                    )
                } $currentYear",
                style = MaterialTheme.typography.headlineSmall
            )
            IconButton(onClick = {
                if (currentMonth == 11) {
                    onMonthChange(0, currentYear + 1)
                } else {
                    onMonthChange(currentMonth + 1, currentYear)
                }
            }) {
                Icon(Icons.Default.ArrowForward, contentDescription = "Mes siguiente")
            }
        }

        // Días de la semana
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            daysOfWeek.forEach { day ->
                Text(
                    text = day,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
            }
        }

        // Vista mensual
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Column {
                for (week in 0..5) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        for (day in 0..6) {
                            val dayOfMonth = week * 7 + day - firstDayOfWeek + 1
                            if (dayOfMonth in 1..daysInMonth) {
                                val date =
                                    calendar.apply { set(Calendar.DAY_OF_MONTH, dayOfMonth) }.time
                                val hasEvent = events.any {
                                    it.date != null && dateFormat.format(it.date) == dateFormat.format(
                                        date
                                    )
                                }
                                val isToday = dateFormat.format(date) == dateFormat.format(today)
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .background(
                                            color = if (isToday) Color.LightGray.copy(alpha = 0.5f) else Color.Transparent,
                                            shape = CircleShape // Redondear la selección
                                        )
                                        .clickable { onDateSelected(date) },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(
                                            text = dayOfMonth.toString(),
                                            color = MaterialTheme.colorScheme.onBackground,
                                            fontSize = 14.sp
                                        )
                                        if (hasEvent) {
                                            Box(
                                                modifier = Modifier
                                                    .size(6.dp)
                                                    .background(Color.Red, shape = CircleShape)
                                            )
                                        }
                                    }
                                }
                            } else {
                                Box(modifier = Modifier.size(40.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EventItem(
    event: Event,
    isCompleted: Boolean,
    onEditEvent: (Event) -> Unit,
    onDeleteEvent: () -> Unit,
    onCompleteEvent: () -> Unit
) {
    val textColor =
        if (isCompleted) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f) else MaterialTheme.colorScheme.onSurface

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { if (!isCompleted) onEditEvent(event) }
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .weight(0.15f)
                    .fillMaxHeight()
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Checkbox(checked = isCompleted, onCheckedChange = { onCompleteEvent() })
            }
            Column(
                modifier = Modifier
                    .weight(0.7f)
                    .padding(vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(event.title, style = MaterialTheme.typography.bodyLarge, color = textColor)
                    event.date?.let { date ->
                        Text(
                            SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(date),
                            style = MaterialTheme.typography.bodySmall,
                            color = textColor
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    event.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = textColor
                )
            }
            Box(
                modifier = Modifier
                    .weight(0.15f)
                    .fillMaxHeight()
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                IconButton(onClick = { onDeleteEvent() }) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar Evento")
                }
            }
        }
    }
}

@Composable
fun AddEventDialog(
    event: Event? = null,
    onAddEvent: (Event) -> Unit,
    onDismiss: () -> Unit,
    isEditable: Boolean
) {
    var title by remember { mutableStateOf(event?.title ?: "") }
    var description by remember { mutableStateOf(event?.description ?: "") }
    var date by remember { mutableStateOf(event?.date) }
    val context = LocalContext.current

    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            calendar.set(year, month, dayOfMonth)
            date = calendar.time
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(if (event != null) "Editar Evento" else "Agregar Evento") },
        text = {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Seleccionar Fecha", style = MaterialTheme.typography.bodyLarge)
                    IconButton(onClick = { if (isEditable) datePickerDialog.show() }) {
                        Icon(Icons.Default.DateRange, contentDescription = "Seleccionar Fecha")
                    }
                }
                if (date != null) {
                    Text(
                        "Fecha seleccionada: ${
                            SimpleDateFormat(
                                "dd/MM/yyyy",
                                Locale.getDefault()
                            ).format(date)
                        }", style = MaterialTheme.typography.bodyMedium
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Título") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = isEditable
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Descripción") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    enabled = isEditable
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                val newEvent = Event(
                    id = event?.id ?: UUID.randomUUID().hashCode(),
                    title = title,
                    description = description,
                    date = date
                )
                onAddEvent(newEvent)
            }, enabled = isEditable) {
                Text(if (event != null) "Actualizar" else "Agregar")
            }
        },
        dismissButton = {
            Button(onClick = { onDismiss() }) {
                Text("Cancelar")
            }
        }
    )
}
