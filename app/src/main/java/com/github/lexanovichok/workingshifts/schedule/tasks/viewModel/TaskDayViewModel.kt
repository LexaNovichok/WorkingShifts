package com.github.lexanovichok.workingshifts.schedule.tasks.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.lexanovichok.workingshifts.main.NavigationA
import com.github.lexanovichok.workingshifts.schedule.userData.Task
import com.github.lexanovichok.workingshifts.schedule.tasks.model.TaskRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class TaskDayViewModel(
    private val taskRepository: TaskRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val dispatcherMain: CoroutineDispatcher = Dispatchers.Main.immediate,
) : ViewModel() {

    private val _tasks = MutableLiveData<List<Task>>()
    val tasks: LiveData<List<Task>> get() = _tasks
    private val viewModelScope = CoroutineScope(dispatcherMain + SupervisorJob())
    init {
        fetchTasks()
    }

    fun fetchTasks() {
        viewModelScope.launch(dispatcher) {
            taskRepository.getTasks()
        }
    }

    fun addOrUpdateTask(task: Task) {
        viewModelScope.launch(dispatcher) {
            taskRepository.createOrUpdateTask(task)
        }
    }

    fun deleteTask(taskId: String) {
        viewModelScope.launch(dispatcher) {
            taskRepository.deleteTask(taskId)
        }
    }

}
