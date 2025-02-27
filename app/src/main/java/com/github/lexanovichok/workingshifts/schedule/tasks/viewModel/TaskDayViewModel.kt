package com.github.lexanovichok.workingshifts.schedule.tasks.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.lexanovichok.workingshifts.schedule.main.NavigationF
import com.github.lexanovichok.workingshifts.schedule.tasks.core.TaskAddScreen
import com.github.lexanovichok.workingshifts.schedule.tasks.core.TaskInfoLiveDataWrapper
import com.github.lexanovichok.workingshifts.schedule.tasks.core.TaskInfoScreenF
import com.github.lexanovichok.workingshifts.schedule.tasks.core.TaskListLiveDataWrapper
import com.github.lexanovichok.workingshifts.schedule.userData.Task
import com.github.lexanovichok.workingshifts.schedule.tasks.model.TaskRepository
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TaskDayViewModel(
    private val navigationF : NavigationF.Update,
    private val taskRepository: TaskRepository,
    private val taskListLiveDataWrapper : TaskListLiveDataWrapper.Mutable,
    private val taskInfoLiveDataWrapper: TaskInfoLiveDataWrapper.Mutable,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val dispatcherMain: CoroutineDispatcher = Dispatchers.Main.immediate,
) : ViewModel() {

    private val _errorMessage = MutableLiveData<String?>()  // Для хранения ошибок
    val errorMessage : LiveData<String?> get() = _errorMessage

    private val _date = MutableLiveData<String?>()


    private val viewModelScope = CoroutineScope(dispatcherMain + SupervisorJob())

    init {
        taskRepository.tasksRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                viewModelScope.launch(dispatcher) {
                    val list = taskRepository.getTasks()
                    withContext(dispatcherMain) {
                        taskListLiveDataWrapper.update(list)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("SCHEDULE", "TaskDayViewModel ${error.message}")
            }

        })
    }


    fun tasksListLiveData() : LiveData<List<Task>> = taskListLiveDataWrapper.liveData()

    fun updateCurrentTaskFromRcView(task : Task) {
        taskInfoLiveDataWrapper.update(task)
    }
    fun dateLiveData() : LiveData<String?> = _date

    fun setDateToLiveData(date : String?) {
        _date.value = date
    }

    fun updateTask(task: Task) {
        viewModelScope.launch(dispatcher) {
            taskRepository.updateTask(task)
        }
    }

    fun deleteTask(taskId: String) {
        viewModelScope.launch(dispatcher) {
            taskRepository.deleteTask(taskId)
        }
    }

    fun addTask(task : Task) {
        viewModelScope.launch(dispatcher) {
            try {
                taskRepository.addTask(task)
            } catch (e: Exception) {
                withContext(dispatcherMain) {
                    _errorMessage.value = "Ошибка добавления работника: ${e.message}"
                }
            }
        }
    }

    fun addTaskFragment() {
        navigationF.update(TaskAddScreen)
    }

    fun infoTaskFragment() {
        navigationF.update(TaskInfoScreenF)
    }

}
