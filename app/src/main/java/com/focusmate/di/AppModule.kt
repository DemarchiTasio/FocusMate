// AppModule.kt
package com.focusmate.di

import android.content.Context
import androidx.room.Room
import com.focusmate.data.local.AppDatabase
import com.focusmate.data.local.EventDao
import com.focusmate.data.local.HabitCompletionDao
import com.focusmate.data.local.HabitDao
import com.focusmate.data.model.Event
import com.focusmate.data.repository.EventRepository
import com.focusmate.data.repository.HabitCompletionRepository
import com.focusmate.data.repository.HabitRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "focusmate_db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideEventDao(db: AppDatabase): EventDao {
        return db.eventDao()
    }

    @Provides
    fun provideHabitDao(db: AppDatabase) = db.habitDao()

    @Provides
    fun provideHabitCompletionDao(db: AppDatabase) = db.habitCompletionDao()

    @Provides
    @Singleton
    fun provideEventRepository(eventDao: EventDao): EventRepository {
        return EventRepository(eventDao)
    }

    @Provides
    @Singleton
    fun provideHabitRepository(habitDao: HabitDao) = HabitRepository(habitDao)

    @Provides
    @Singleton
    fun provideHabitCompletionRepository(habitCompletionDao: HabitCompletionDao) =
        HabitCompletionRepository(habitCompletionDao)

    @Provides
    @Singleton
    fun providePredefinedEvents(): List<Event> {
        return listOf(
            Event(id = 1, title = "Evento 1", description = "Descripción 1", date = Date(1720875550000), isCompleted = false),
            Event(id = 2, title = "Evento 2", description = "Descripción 2", date = Date(1720875550000), isCompleted = false),
            Event(id = 3, title = "Evento 3", description = "Descripción 3", date = Date(1720875550000), isCompleted = false),
            Event(id = 4, title = "Evento 4", description = "Descripción 4", date = Date(1720875550000), isCompleted = false),
            Event(id = 5, title = "Evento 5", description = "Descripción 5", date = Date(1720875550000), isCompleted = false),
            Event(id = 6, title = "Evento 6", description = "Descripción 6", date = Date(1720875550000), isCompleted = false),
            Event(id = 7, title = "Evento 7", description = "Descripción 7", date = Date(1720875550000), isCompleted = false),
            Event(id = 8, title = "Evento 8", description = "Descripción 8", date = Date(1720875550000), isCompleted = false),
            Event(id = 9, title = "Evento 9", description = "Descripción 9", date = Date(1720875550000), isCompleted = false),
            Event(id = 10, title = "Evento 10", description = "Descripción 10", date = Date(1720875550000), isCompleted = false)
        )
    }
}
