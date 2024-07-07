package com.focusmate.ui.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(val title: String, val icon: ImageVector, val route: String) {
    object Calendar : BottomNavItem("Calendar", Icons.Default.DateRange, "calendar")
    object Habits : BottomNavItem("Habits", Icons.Default.Check, "habits")
    object Mindfulness : BottomNavItem("Mindfulness", Icons.Default.Send, "mindfulness")
    object Chat : BottomNavItem("Chat", Icons.Default.Check, "chat")
    object Statistics : BottomNavItem("Statistics", Icons.Default.Star, "statistics")
}
