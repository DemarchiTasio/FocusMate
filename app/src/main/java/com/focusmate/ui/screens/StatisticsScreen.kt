package com.focusmate.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

data class StatisticItem(val title: String, val value: String)

@Composable
fun StatisticsScreen(navController: NavHostController) {
    val eventStatistics = listOf(
        StatisticItem("Total de Eventos", "45"),
        StatisticItem("Eventos Completados", "30"),
        StatisticItem("Eventos por Categoría", "Trabajo: 20, Personal: 15, Salud: 10"),
        StatisticItem("Eventos por Mes", "Enero: 5, Febrero: 10, Marzo: 8, ..."),
        StatisticItem("Eventos Próximos", "3")
    )

    val habitStatistics = listOf(
        StatisticItem("Total de Hábitos", "20"),
        StatisticItem("Hábitos Completados", "15"),
        StatisticItem("Frecuencia de Hábitos", "Diario: 10, Semanal: 5"),
        StatisticItem("Días Activos", "12"),
        StatisticItem("Hábitos por Categoría", "Salud: 10, Trabajo: 5, Personal: 5")
    )

    Scaffold(
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Estadísticas",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Text(
                    text = "Eventos",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                LazyColumn {
                    items(eventStatistics) { statistic ->
                        StatisticItemView(statistic)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Hábitos",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                LazyColumn {
                    items(habitStatistics) { statistic ->
                        StatisticItemView(statistic)
                    }
                }
            }
        }
    )
}

@Composable
fun StatisticItemView(statistic: StatisticItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(statistic.title, style = MaterialTheme.typography.bodyLarge)
            Text(statistic.value, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
