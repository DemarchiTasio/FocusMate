package com.focusmate.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.focusmate.data.model.Habit
import com.focusmate.data.model.HabitCompletion
import com.focusmate.data.repository.HabitCompletionRepository
import com.focusmate.data.repository.HabitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val habitCompletionRepository: HabitCompletionRepository
) : ViewModel() {

    val habitCompletions: LiveData<List<HabitCompletion>> = habitCompletionRepository.getAllHabitCompletions()

    val totalHabitsCompleted: LiveData<Int> = habitCompletionRepository.getTotalHabitsCompleted()

    val totalEventsCompleted: LiveData<Int> = habitCompletionRepository.getTotalEventsCompleted()

    fun insertHabitCompletion(habitCompletion: HabitCompletion) {
        viewModelScope.launch {
            habitCompletionRepository.insert(habitCompletion)
        }
    }
}
