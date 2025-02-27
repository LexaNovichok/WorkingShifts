package com.github.lexanovichok.workingshifts.schedule.tasks.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.github.lexanovichok.workingshifts.core.ClearViewModel
import com.github.lexanovichok.workingshifts.schedule.address.core.AddressInfoLiveDataWrapper
import com.github.lexanovichok.workingshifts.schedule.address.core.AddressInfoScreenF
import com.github.lexanovichok.workingshifts.schedule.address.core.AddressesListLiveDataWrapper
import com.github.lexanovichok.workingshifts.schedule.address.model.AddressRepository
import com.github.lexanovichok.workingshifts.schedule.main.NavigationF
import com.github.lexanovichok.workingshifts.schedule.main.ScreenF
import com.github.lexanovichok.workingshifts.schedule.tasks.core.TaskInfoLiveDataWrapper
import com.github.lexanovichok.workingshifts.schedule.tasks.core.TaskListLiveDataWrapper
import com.github.lexanovichok.workingshifts.schedule.tasks.model.TaskRepository
import com.github.lexanovichok.workingshifts.schedule.userData.Address
import com.github.lexanovichok.workingshifts.schedule.userData.Task
import com.github.lexanovichok.workingshifts.schedule.userData.Worker
import com.github.lexanovichok.workingshifts.schedule.worker.core.WorkerInfoLiveDataWrapper
import com.github.lexanovichok.workingshifts.schedule.worker.core.WorkerInfoScreenF
import com.github.lexanovichok.workingshifts.schedule.worker.model.WorkersRepository
import com.github.lexanovichok.workingshifts.schedule.worker.viewModel.WorkerInfoViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TaskInfoViewModel(
    private val navigationF : NavigationF.Update,
    private val clear: ClearViewModel,
    private val workersRepository: WorkersRepository,
    private val workerInfoLiveDataWrapper: WorkerInfoLiveDataWrapper.Mutable,
    private val addressesRepository: AddressRepository,
    private val addressInfoLiveDataWrapper: AddressInfoLiveDataWrapper.Mutable,
    private val tasksRepository: TaskRepository,
    private val taskListLiveDataWrapper: TaskListLiveDataWrapper.Mutable,
    private val taskInfoLiveDataWrapper : TaskInfoLiveDataWrapper.Mutable,
    private val dispatcher : CoroutineDispatcher = Dispatchers.IO,
    private val dispatcherMain : CoroutineDispatcher = Dispatchers.Main
) : ViewModel() {

    private val viewModelScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    fun getWorkerById(workerId : String) {
        viewModelScope.launch(dispatcher) {
            try {
                val worker = workersRepository.getWorkerById(workerId)
                Log.d("SCHEDULE", "WorkersViewModel getWorkerById. ID: $workerId, Name: ${worker?.name}")

                withContext(dispatcherMain) {
                    if (worker != null) {
                        workerInfoLiveDataWrapper.update(worker)
                    } else {
                        Log.d("SCHEDULE", "worker with ID $workerId not found")
                    }
                }
            } catch(e : Exception) {
                Log.e("SCHEDULE", "Error fetching worker with ID $workerId: ${e.message}")
            }
        }
    }

    fun getAddressById(addressId : String) {
        viewModelScope.launch(dispatcher) {
            try {
                val address = addressesRepository.getAddressById(addressId)

                withContext(dispatcherMain) {
                    if (address != null) {
                        addressInfoLiveDataWrapper.update(address)
                    }
                }
            } catch (e : Exception) {
                Log.e("SCHEDULE", "Error fetching address with ID $addressId: ${e.message}")
            }
        }
    }

    fun updateTask(task : Task) {
        viewModelScope.launch(dispatcher) {
            tasksRepository.updateTask(task)
            withContext(dispatcherMain) {
                taskInfoLiveDataWrapper.update(task)
            }
        }
        comeback()
    }

    fun deleteTask(taskId : String) {
        viewModelScope.launch(dispatcher) {
            tasksRepository.deleteTask(taskId)
            withContext(dispatcherMain) {
                comeback()
            }
        }
    }

    fun workerInfoLiveData() : LiveData<Worker> = workerInfoLiveDataWrapper.liveData()
    fun updateCurrentWorkerFromRcView(worker: Worker) {
        workerInfoLiveDataWrapper.update(worker)
    }

    fun addressInfoLiveData() : LiveData<Address> = addressInfoLiveDataWrapper.liveData()
    fun updateCurrentAddressFromRcView(address: Address) {
        addressInfoLiveDataWrapper.update(address)
    }

    fun taskInfoLiveData() : LiveData<Task> = taskInfoLiveDataWrapper.liveData()

    fun workerInfoFragment() {
        navigationF.update(WorkerInfoScreenF)
    }

    fun addressInfoFragment() {
        navigationF.update(AddressInfoScreenF)
    }

    private fun comeback() {
        navigationF.update(ScreenF.Pop)
        clear.clearViewModel(TaskInfoViewModel::class.java)
    }

}