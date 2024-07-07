package com.focusmate.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "habit")
data class Habit(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val startDate: Date,
    val frequency: String,
    val daysOfWeek: List<Int>?, // Para frecuencia semanal
    val daysOfMonth: List<Int>?, // Para frecuencia mensual
    val completedDates: List<Date>
)
