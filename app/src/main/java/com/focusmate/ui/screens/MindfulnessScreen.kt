package com.focusmate.ui.screens

import android.media.MediaPlayer
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.focusmate.R
import kotlinx.coroutines.delay

data class Meditation(
    val id: Int,
    val title: String,
    val duration: String,
    val audioResId: Int,
    val imageResId: Int? = null // Agrega un campo para la referencia de imagen
)

data class BreathingExercise(
    val id: Int,
    val title: String,
    val duration: String,
    val audioResId: Int,
    val imageResId: Int? = null // Agrega un campo para la referencia de imagen
)

@Composable
fun MindfulnessScreen(navController: NavHostController) {
    val meditations by remember {
        mutableStateOf(
            listOf(
                Meditation(id = 1, title = "Meditación de la mañana", duration = "10 min", audioResId = R.raw.meditacion, imageResId = R.drawable.placeholder),
                Meditation(id = 2, title = "Meditación para la concentración", duration = "15 min", audioResId = R.raw.meditacion, imageResId = R.drawable.placeholder),
                Meditation(id = 3, title = "Meditación de la noche", duration = "20 min", audioResId = R.raw.meditacion, imageResId = R.drawable.placeholder),
                Meditation(id = 4, title = "Meditación de la noche", duration = "20 min", audioResId = R.raw.meditacion, imageResId = R.drawable.placeholder),
                Meditation(id = 5, title = "Meditación de la noche", duration = "20 min", audioResId = R.raw.meditacion, imageResId = R.drawable.placeholder),
                Meditation(id = 6, title = "Meditación de la noche", duration = "20 min", audioResId = R.raw.meditacion, imageResId = R.drawable.placeholder),
                Meditation(id = 7, title = "Meditación de la noche", duration = "20 min", audioResId = R.raw.meditacion, imageResId = R.drawable.placeholder),
                Meditation(id = 8, title = "Meditación de la noche", duration = "20 min", audioResId = R.raw.meditacion, imageResId = R.drawable.placeholder)
            )
        )
    }
    val breathingExercises by remember {
        mutableStateOf(
            listOf(
                BreathingExercise(id = 1, title = "Respiración profunda", duration = "5 min", audioResId = R.raw.meditacion, imageResId = R.drawable.placeholder),
                BreathingExercise(id = 2, title = "Respiración alterna", duration = "7 min", audioResId = R.raw.meditacion, imageResId = R.drawable.placeholder),
                BreathingExercise(id = 3, title = "Respiración de caja", duration = "10 min", audioResId = R.raw.meditacion, imageResId = R.drawable.placeholder)
            )
        )
    }
    var isPlaying by remember { mutableStateOf(false) }
    var currentMeditation by remember { mutableStateOf<Meditation?>(null) }
    var currentBreathingExercise by remember { mutableStateOf<BreathingExercise?>(null) }
    var selectedCategory by remember { mutableStateOf("Respiración") }

    Scaffold(
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Mindfulness",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Categoría selection
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

                Spacer(modifier = Modifier.height(8.dp))

                if (selectedCategory == "Respiración") {
                    LazyColumn(modifier = Modifier.height(300.dp)) {
                        items(breathingExercises) { exercise ->
                            BreathingExerciseItem(
                                exercise = exercise,
                                isPlaying = isPlaying && currentBreathingExercise == exercise,
                                onPlayPauseClick = {
                                    if (isPlaying && currentBreathingExercise == exercise) {
                                        isPlaying = false
                                        currentBreathingExercise = null
                                    } else {
                                        isPlaying = true
                                        currentBreathingExercise = exercise
                                        currentMeditation = null
                                    }
                                }
                            )
                        }
                    }
                } else {
                    LazyColumn(modifier = Modifier.height(300.dp)) {
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
                                        currentBreathingExercise = null
                                    }
                                }
                            )
                        }
                    }
                }

                currentMeditation?.let {
                    AudioPlayer(
                        audioResId = it.audioResId, // Usar el recurso de audio de la meditación seleccionada
                        title = it.title,
                        isPlaying = isPlaying,
                        onPlayPauseClick = {
                            isPlaying = !isPlaying
                        },
                        imageResId = it.imageResId
                    )
                }

                currentBreathingExercise?.let {
                    AudioPlayer(
                        audioResId = it.audioResId, // Usar el recurso de audio de la guía de respiración seleccionada
                        title = it.title,
                        isPlaying = isPlaying,
                        onPlayPauseClick = {
                            isPlaying = !isPlaying
                        },
                        imageResId = it.imageResId
                    )
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
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(meditation.title, style = MaterialTheme.typography.bodyLarge)
                Text(meditation.duration, style = MaterialTheme.typography.bodyMedium)
            }
            IconButton(onClick = { onPlayPauseClick() }) {
                Icon(

                            painterResource(R.drawable.play),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
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
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(exercise.title, style = MaterialTheme.typography.bodyLarge)
                Text(exercise.duration, style = MaterialTheme.typography.bodyMedium)
            }
            IconButton(onClick = { onPlayPauseClick() }) {
                Icon(

                    painterResource(R.drawable.play),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
fun AudioPlayer(
    audioResId: Int,
    title: String,
    isPlaying: Boolean,
    onPlayPauseClick: () -> Unit,
    imageResId: Int? = null // Agregar parámetro para la referencia de imagen
) {
    val context = LocalContext.current
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }
    var isPrepared by remember { mutableStateOf(false) }
    var currentPosition by remember { mutableStateOf(0) }

    LaunchedEffect(audioResId) {
        mediaPlayer = MediaPlayer.create(context, audioResId).apply {
            setOnPreparedListener {
                isPrepared = true
                if (isPlaying) start()
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer?.release()
        }
    }

    LaunchedEffect(mediaPlayer, isPlaying) {
        while (isPlaying) {
            currentPosition = mediaPlayer?.currentPosition ?: 0
            delay(1000)
        }
    }

    if (isPrepared) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = title, style = MaterialTheme.typography.headlineSmall)

            if (imageResId != null) {
                Image(
                    painter = painterResource(id = imageResId),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = {
                    mediaPlayer?.seekTo((mediaPlayer?.currentPosition ?: 0) - 10000)
                }) {
                    Icon(

                        painterResource(R.drawable.backward10),
                        contentDescription = null,
                        modifier = Modifier.size(32.dp)
                    )
                }
                IconButton(onClick = {
                    if (isPlaying) {
                        mediaPlayer?.pause()
                    } else {
                        mediaPlayer?.start()
                    }
                    onPlayPauseClick()
                }) {
                    if (isPlaying){
                        Icon(
                            painterResource(R.drawable.pause),
                            contentDescription = null,
                            modifier = Modifier.size(32.dp)
                        )
                    }else{
                        Icon(
                            painterResource(R.drawable.play),
                            contentDescription = null,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
                IconButton(onClick = {
                    mediaPlayer?.seekTo((mediaPlayer?.currentPosition ?: 0) + 10000)
                }) {
                    Icon(
                        painterResource(R.drawable.forward10),
                        contentDescription = null,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
            Text(
                text = String.format(
                    "%02d:%02d",
                    currentPosition / 1000 / 60,
                    currentPosition / 1000 % 60
                ),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
