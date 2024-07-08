package com.focusmate.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

data class Meditation(
    val id: Int,
    val title: String,
    val duration: String
)

data class BreathingExercise(
    val id: Int,
    val title: String,
    val duration: String
)

@Composable
fun MindfulnessScreen(navController: NavHostController) {
    var meditations by remember { mutableStateOf(
        listOf(
            Meditation(id = 1, title = "Meditación de la mañana", duration = "10 min"),
            Meditation(id = 2, title = "Meditación para la concentración", duration = "15 min"),
            Meditation(id = 3, title = "Meditación de la noche", duration = "20 min")
        )
    ) }
    var breathingExercises by remember { mutableStateOf(
        listOf(
            BreathingExercise(id = 1, title = "Respiración profunda", duration = "5 min"),
            BreathingExercise(id = 2, title = "Respiración alterna", duration = "7 min"),
            BreathingExercise(id = 3, title = "Respiración de caja", duration = "10 min")
        )
    ) }
    var isPlaying by remember { mutableStateOf(false) }
    var currentMeditation by remember { mutableStateOf<Meditation?>(null) }
    var selectedCategory by remember { mutableStateOf("Respiración") }

    Scaffold(
        content = { innerPadding ->
            Column(modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)) {
                Text(
                    text = "Mindfulness",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Frecuencia selection
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    Text(
                        text = "Respiración",
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (selectedCategory == "Respiración") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.clickable { selectedCategory = "Respiración" }
                    )
                    Text(
                        text = "Meditaciones",
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (selectedCategory == "Meditaciones") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.clickable { selectedCategory = "Meditaciones" }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (selectedCategory == "Respiración") {
                    LazyColumn {
                        items(breathingExercises) { exercise ->
                            BreathingExerciseItem(
                                exercise = exercise,
                                isPlaying = isPlaying && currentMeditation == null,
                                onPlayPauseClick = {
                                    if (isPlaying && currentMeditation == null) {
                                        isPlaying = false
                                    } else {
                                        isPlaying = true
                                    }
                                }
                            )
                        }
                    }
                } else {
                    LazyColumn {
                        items(meditations) { meditation ->
                            MeditationItem(
                                meditation = meditation,
                                isPlaying = isPlaying && currentMeditation == meditation,
                                onPlayPauseClick = {
                                    if (isPlaying && currentMeditation == meditation) {
                                        isPlaying = false
                                        currentMeditation = null
                                    } else {
                                        isPlaying = true
                                        currentMeditation = meditation
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun MeditationItem(
    meditation: Meditation,
    isPlaying: Boolean,
    onPlayPauseClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onPlayPauseClick() }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(meditation.title, style = MaterialTheme.typography.bodyLarge)
                Text(meditation.duration, style = MaterialTheme.typography.bodyMedium)
            }
            IconButton(onClick = { onPlayPauseClick() }) {
                Icon(
                    if (isPlaying) Icons.Default.Refresh else Icons.Default.PlayArrow,
                    contentDescription = if (isPlaying) "Pause" else "Play"
                )
            }
        }
    }
}

@Composable
fun BreathingExerciseItem(
    exercise: BreathingExercise,
    isPlaying: Boolean,
    onPlayPauseClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onPlayPauseClick() }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(exercise.title, style = MaterialTheme.typography.bodyLarge)
                Text(exercise.duration, style = MaterialTheme.typography.bodyMedium)
            }
            IconButton(onClick = { onPlayPauseClick() }) {
                Icon(
                    if (isPlaying) Icons.Default.Refresh else Icons.Default.PlayArrow,
                    contentDescription = if (isPlaying) "Pause" else "Play"
                )
            }
        }
    }
}
