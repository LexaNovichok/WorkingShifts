package com.github.lexanovichok.workingshifts.schedule.tasks.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.lexanovichok.workingshifts.core.ClearViewModel
import com.github.lexanovichok.workingshifts.schedule.address.core.AddressesListLiveDataWrapper
import com.github.lexanovichok.workingshifts.schedule.address.model.AddressRepository
import com.github.lexanovichok.workingshifts.schedule.address.viewModel.AddAddressesViewModel
import com.github.lexanovichok.workingshifts.schedule.main.NavigationF
import com.github.lexanovichok.workingshifts.schedule.main.ScreenF
import com.github.lexanovichok.workingshifts.schedule.tasks.core.TaskListLiveDataWrapper
import com.github.lexanovichok.workingshifts.schedule.tasks.model.TaskRepository
import com.github.lexanovichok.workingshifts.schedule.userData.Address
import com.github.lexanovichok.workingshifts.schedule.userData.Task
import com.github.lexanovichok.workingshifts.schedule.userData.Worker
import com.github.lexanovichok.workingshifts.schedule.worker.core.WorkersListLiveDataWrapper
import com.github.lexanovichok.workingshifts.schedule.worker.model.WorkersRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TaskAddViewModel(
    private val navigationF : NavigationF.Update,
    private val clear: ClearViewModel,
    private val workersRepository: WorkersRepository,
    private val workersListLiveDataWrapper: WorkersListLiveDataWrapper.Mutable,
    private val addressesRepository: AddressRepository,
    private val addressesListLiveDataWrapper: AddressesListLiveDataWrapper.Mutable,
    private val tasksRepository: TaskRepository,
    private val taskListLiveDataWrapper: TaskListLiveDataWrapper.Mutable,
    private val dispatcher : CoroutineDispatcher = Dispatchers.IO,
    private val dispatcherMain : CoroutineDispatcher = Dispatchers.Main
) : ViewModel() {

    private val viewModelScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    private val _errorMessage = MutableLiveData<String?>()  // Для хранения ошибок
    val errorMessage : LiveData<String?> get() = _errorMessage


    fun addTask(task: Task) {
        viewModelScope.launch(dispatcher) {
            try {
                tasksRepository.addTask(task)
            } catch (e : Exception) {
                withContext(dispatcherMain) {
                    _errorMessage.value = "Ошибка добавления работника: ${e.message}"
                }
            }
        }
    }

    fun getWorkers() {
        viewModelScope.launch(dispatcher) {
            val list = workersRepository.getWorkers()

            withContext(dispatcherMain) {
                workersListLiveDataWrapper.update(list)
            }
        }
    }

    fun getAddresses() {
        viewModelScope.launch(dispatcher) {
            val list = addressesRepository.getAddresses()

            withContext(dispatcherMain) {
                addressesListLiveDataWrapper.update(list)
            }
        }
    }

    fun workersListLiveData() = workersListLiveDataWrapper.liveData()
    fun addressesListLiveData() = addressesListLiveDataWrapper.liveData()
    fun comeback() {
        navigationF.update(ScreenF.Pop)
        clear.clearViewModel(TaskAddViewModel::class.java)
        Log.d("NAVIGATION", "TaskAddViewModel update to: ScreenF.Pop")
    }
}