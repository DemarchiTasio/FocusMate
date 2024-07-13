package com.focusmate.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "events")
data class Event(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val date: Date?,
    val isCompleted: Boolean = false,
    val tag: String? = null // Nuevo campo para la etiqueta
)
