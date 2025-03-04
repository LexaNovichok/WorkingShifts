package com.github.lexanovichok.workingshift.schedule.tasks.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.github.lexanovichok.workingshift.schedule.main.NavigationF
import com.github.lexanovichok.workingshift.schedule.tasks.core.TasksHistoryLiveDataWrapper
import com.github.lexanovichok.workingshift.schedule.tasks.model.TaskRepository
import com.github.lexanovichok.workingshift.schedule.userData.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class TaskHistoryViewModel(
    private val navigationF: NavigationF.Mutable,
    private val taskRepository: TaskRepository,
    private val tasksHistoryLiveDataWrapper: TasksHistoryLiveDataWrapper.Mutable,
    private val dispatcher : CoroutineDispatcher = Dispatchers.IO,
    private val dispatcherMain : CoroutineDispatcher = Dispatchers.Main.immediate,
) : ViewModel() {

    private val viewModelScope = CoroutineScope(SupervisorJob() + dispatcher)

    init {
        taskRepository.tasksRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                viewModelScope.launch(dispatcher) {
                    val list = taskRepository.getTasks()
                    getTasksHistory(list)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("SCHEDULE", "TaskDayViewModel ${error.message}")
            }

        })
    }

    fun tasksHistoryLiveData() = tasksHistoryLiveDataWrapper.liveData()

    private fun getTasksHistory(allTasks : List<Task>) {
        viewModelScope.launch(dispatcher) {
            val filteredTasks = filterTasksBeforeDate(allTasks, daysBefore = 1)
            val sortedTasks = filteredTasks.sortedByDescending { task ->
                parseDate(task.date)
            }
            withContext(dispatcherMain) {
                tasksHistoryLiveDataWrapper.update(sortedTasks)
            }
        }
    }

    private fun filterTasksBeforeDate(tasks: List<Task>, daysBefore: Int): List<Task> {
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -daysBefore)
        val targetDate = calendar.time

        return tasks.filter { task ->
            try {
                val taskDate = dateFormatter.parse(task.date) // Попытка парсинга даты задачи
                Log.d("SCHEDULE", "filterTasksBeforeDate taskDate = $taskDate")
                taskDate?.before(targetDate) == true // Проверяем, раньше ли дата задачи целевой даты
            } catch (e: ParseException) {
                Log.e("SCHEDULE", "Ошибка парсинга даты: ${task.date}", e)
                false // Если дата не парсится, исключаем задачу
            }
        }
    }

    private fun parseDate(date: String): Date? {
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return try {
            dateFormatter.parse(date)
        } catch (e: ParseException) {
            Log.e("SCHEDULE", "Ошибка парсинга даты: $date", e)
            null
        }
    }
}

