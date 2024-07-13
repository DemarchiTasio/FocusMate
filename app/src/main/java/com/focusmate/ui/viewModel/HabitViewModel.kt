package com.focusmate.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.focusmate.data.model.Event
import com.focusmate.data.model.Habit
import com.focusmate.data.model.HabitCompletion
import com.focusmate.data.repository.HabitRepository
import com.focusmate.data.repository.HabitCompletionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HabitViewModel @Inject constructor(
    private val habitRepository: HabitRepository,
    private val habitCompletionRepository: HabitCompletionRepository
) : ViewModel() {

//    private val _allHabits = MutableStateFlow<List<Habit>>(emptyList())
//    val allHabits: StateFlow<List<Habit>> = _allHabits

    val allHabits: LiveData<List<Habit>> = habitRepository.getAllHabits().asLiveData()

//    init {
//        viewModelScope.launch {
//            habitRepository.getAllHabits().collect {
//                _allHabits.value = it
//            }
//        }
//    }

    fun insert(habit: Habit) = viewModelScope.launch {
        habitRepository.insertHabit(habit)
    }

    fun update(habit: Habit) = viewModelScope.launch {
        habitRepository.updateHabit(habit)
    }

    fun delete(habit: Habit) = viewModelScope.launch {
        habitRepository.deleteHabit(habit.id)
    }

    fun insertHabitCompletion(habitId: Int) = viewModelScope.launch {
        val completion = HabitCompletion(habitId = habitId, completionDate = Date().time)
        habitCompletionRepository.insertHabitCompletion(completion)
    }

    fun deleteHabitCompletion(habitId: Int) = viewModelScope.launch {
        habitCompletionRepository.getHabitCompletions(habitId).collect { completions ->
            completions.lastOrNull()?.let {
                habitCompletionRepository.deleteHabitCompletion(it.id)
            }
        }
    }

    fun removeHabitCompletion(habitId: Int) = viewModelScope.launch {
        habitCompletionRepository.deleteByHabitId(habitId)
    }
}
