package com.github.lexanovichok.workingshifts.schedule.tasks.viewModel

import androidx.lifecycle.ViewModel
import com.github.lexanovichok.workingshifts.schedule.main.NavigationF
import com.github.lexanovichok.workingshifts.schedule.tasks.model.TaskRepository
import com.github.lexanovichok.workingshifts.schedule.worker.core.WorkersScreenF
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class TasksViewModel(
    private val navigationF : NavigationF.Update,
    private val taskRepository: TaskRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val dispatcherMain: CoroutineDispatcher = Dispatchers.Main.immediate,
) : ViewModel() {


}