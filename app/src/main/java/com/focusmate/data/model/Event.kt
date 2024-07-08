package com.focusmate.data.model

import java.util.Date

data class Event(
    val id: Int,
    val title: String,
    val description: String,
    val date: Date?,
    val isCompleted: Boolean = false,
    val tag: String? = null // Nuevo campo para la etiqueta
)