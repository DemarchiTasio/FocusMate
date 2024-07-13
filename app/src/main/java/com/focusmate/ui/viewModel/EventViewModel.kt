// EventViewModel.kt
package com.focusmate.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.focusmate.data.model.Event
import com.focusmate.data.repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(
    private val eventRepository: EventRepository
) : ViewModel() {

    val allEvents: LiveData<List<Event>> = eventRepository.getAllEvents().asLiveData()

    fun insert(event: Event) = viewModelScope.launch {
        eventRepository.insert(event)
    }

    fun update(event: Event) = viewModelScope.launch {
        eventRepository.update(event)
    }

    fun delete(event: Event) = viewModelScope.launch {
        eventRepository.delete(event)
    }
}
