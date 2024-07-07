package com.focusmate.data.model

data class Habit(
    val id: Int = 0,
    val title: String,
    val startDate: Long = System.currentTimeMillis(),
    val endDate: Long? = null,
    val frequency: Int = 1,
    val isCompleted: Boolean = false
)
