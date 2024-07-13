package com.focusmate.data.repository

import androidx.lifecycle.LiveData
import com.focusmate.data.local.HabitDao
import com.focusmate.data.local.HabitCompletionDao
import com.focusmate.data.model.Event
import com.focusmate.data.model.Habit
import com.focusmate.data.model.HabitCompletion
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton


class HabitRepository @Inject constructor(
    private val habitDao: HabitDao
) {

    fun getAllHabits(): Flow<List<Habit>> {
        return habitDao.getAllHabits()
    }

    suspend fun insertHabit(habit: Habit) {
        habitDao.insertHabit(habit)
    }

    suspend fun updateHabit(habit: Habit) {
        habitDao.updateHabit(habit)
    }

    suspend fun deleteHabit(habitId: Int) {
        habitDao.deleteHabit(habitId)
    }

    suspend fun resetHabitsCompletion() {
        habitDao.resetHabitsCompletion()
    }
}

class HabitCompletionRepository @Inject constructor(
    private val habitCompletionDao: HabitCompletionDao
) {
    fun getHabitCompletions(habitId: Int): Flow<List<HabitCompletion>> = habitCompletionDao.getHabitCompletions(habitId)

    suspend fun insertHabitCompletion(habitCompletion: HabitCompletion) {
        habitCompletionDao.insertHabitCompletion(habitCompletion)
    }

    suspend fun deleteHabitCompletion(completionId: Int) {
        habitCompletionDao.deleteHabitCompletion(completionId)
    }

    suspend fun removeCompletion(habitId: Int) {
        habitCompletionDao.removeCompletion(habitId)
    }

    suspend fun deleteByHabitId(habitId: Int) {
        habitCompletionDao.deleteByHabitId(habitId)
    }
    fun getAllHabitCompletions(): LiveData<List<HabitCompletion>> {
        return habitCompletionDao.getAllHabitCompletions()
    }

    fun getTotalHabitsCompleted(): LiveData<Int> {
        return habitCompletionDao.getTotalHabitsCompleted()
    }

    fun getTotalEventsCompleted(): LiveData<Int> {
        return habitCompletionDao.getTotalEventsCompleted()
    }

    suspend fun insert(habitCompletion: HabitCompletion) {
        habitCompletionDao.insertHabitCompletion(habitCompletion)
    }

}
