package com.focusmate.ui.screens

import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import com.focusmate.R

sealed class BottomNavItem(val title: String, @DrawableRes val icon: Int, val route: String) {
    object Calendar : BottomNavItem("Calendar", R.drawable.calendar1, "calendar")
    object Habits : BottomNavItem("Habits", R.drawable.checklist, "habits")
    object Mindfulness : BottomNavItem("Mindfulness", R.drawable.massage, "mindfulness")
    object Chat : BottomNavItem("Chat", R.drawable.messagechatbot, "chat")
    object Statistics : BottomNavItem("Statistics", R.drawable.chartbar, "statistics")
}
