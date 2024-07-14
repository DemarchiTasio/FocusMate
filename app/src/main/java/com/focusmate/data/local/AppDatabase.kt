package com.focusmate.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.focusmate.data.model.Converters
import com.focusmate.data.model.Event
import com.focusmate.data.model.Habit
import com.focusmate.data.model.HabitCompletion

@Database(entities = [Event::class, Habit::class, HabitCompletion::class], version = 3, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao
    abstract fun habitDao(): HabitDao
    abstract fun habitCompletionDao(): HabitCompletionDao
}