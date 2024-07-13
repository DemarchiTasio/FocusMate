package com.focusmate.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.focusmate.data.model.Habit
import com.focusmate.data.model.HabitCompletion
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {
    @Query("SELECT * FROM habits")
    fun getAllHabits(): Flow<List<Habit>>

    @Insert
    suspend fun insertHabit(habit: Habit)

    @Update
    suspend fun updateHabit(habit: Habit)

    @Query("DELETE FROM habits WHERE id = :habitId")
    suspend fun deleteHabit(habitId: Int)

    @Query("UPDATE habits SET isCompleted = 0")
    suspend fun resetHabitsCompletion()
}

@Dao
interface HabitCompletionDao {
    @Query("SELECT * FROM habit_completions WHERE habitId = :habitId")
    fun getHabitCompletions(habitId: Int): Flow<List<HabitCompletion>>

    @Insert
    suspend fun insertHabitCompletion(habitCompletion: HabitCompletion)

    @Query("DELETE FROM habit_completions WHERE id = :completionId")
    suspend fun deleteHabitCompletion(completionId: Int)

    @Query("DELETE FROM habit_completions WHERE habitId = :habitId")
    suspend fun removeCompletion(habitId: Int)

    @Query("DELETE FROM habit_completions WHERE habitId = :habitId")
    suspend fun deleteByHabitId(habitId: Int)

    @Query("SELECT * FROM habit_completions")
    fun getAllHabitCompletions(): LiveData<List<HabitCompletion>>

    @Query("SELECT COUNT(*) FROM habit_completions")
    fun getTotalHabitsCompleted(): LiveData<Int>

    @Query("SELECT COUNT(*) FROM events WHERE isCompleted = 1")
    fun getTotalEventsCompleted(): LiveData<Int>
}
