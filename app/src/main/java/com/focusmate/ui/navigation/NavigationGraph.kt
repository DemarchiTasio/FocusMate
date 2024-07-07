//package com.focusmate.ui.navigation
//
//import androidx.navigation.NavHostController
//import androidx.navigation.compose.NavHost
//import androidx.navigation.compose.composable
//import com.focusmate.ui.screens.CalendarScreen
//import com.focusmate.ui.screens.HabitsScreen
//import com.focusmate.ui.screens.MindfulnessScreen
//
//@Composable
//fun NavigationGraph(navController: NavHostController) {
//    NavHost(navController, startDestination = BottomNavItem.Calendar.route) {
//        composable(BottomNavItem.Calendar.route) {
//            CalendarScreen(navController)
//        }
//        composable(BottomNavItem.Habits.route) {
//            HabitsScreen(navController)
//        }
//        composable(BottomNavItem.Mindfulness.route) {
//            MindfulnessScreen(navController)
//        }
//    }
//}
//
