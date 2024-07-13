package com.focusmate.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.util.Date

@Entity(tableName = "habits")
data class Habit(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val frequency: String,
    @TypeConverters(Converters::class)
    val daysOfWeek: List<Int>,
    val weeklyCount: Int,
    val isCompleted: Boolean = false,
    val tag: String
)

@Entity(tableName = "habit_completions")
data class HabitCompletion(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val habitId: Int,
    val completionDate: Long
)