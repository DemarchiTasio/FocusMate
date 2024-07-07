package com.focusmate.data.model

data class Habit(
    val id: Int,
    val title: String,
    val frequency: String,
    val daysOfWeek: List<Int>,
    val weeklyCount: Int,
    val isCompleted: Boolean
)
