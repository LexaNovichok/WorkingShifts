package com.github.lexanovichok.workingshift.schedule.worker.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.lexanovichok.workingshift.core.ClearViewModel
import com.github.lexanovichok.workingshift.schedule.main.NavigationF
import com.github.lexanovichok.workingshift.schedule.main.ScreenF
import com.github.lexanovichok.workingshift.schedule.userData.Worker
import com.github.lexanovichok.workingshift.schedule.worker.model.WorkersRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddWorkerViewModel(
    private val navigationF: NavigationF.Update,
    private val clear: ClearViewModel,
    private val workersRepository: WorkersRepository,
    private val dispatcher : CoroutineDispatcher = Dispatchers.IO,
    private val dispatcherMain : CoroutineDispatcher = Dispatchers.Main
) : ViewModel() {

    private val viewModelScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)


    private val _errorMessage = MutableLiveData<String?>()  // Для хранения ошибок
    val errorMessage : LiveData<String?> get() = _errorMessage

    fun addWorker(worker: Worker) {
        viewModelScope.launch(dispatcher) {
            try {
                workersRepository.addWorker(worker)
            } catch (e: Exception) {
                withContext(dispatcherMain) {
                    _errorMessage.value = "Ошибка добавления работника: ${e.message}"
                }
            }
        }
    }

    fun getWorkers(onWorkersReceived: (List<Worker>) -> Unit) {
        viewModelScope.launch(dispatcher) {
            try {
                val workers = workersRepository.getWorkers()
                withContext(dispatcherMain) {
                    onWorkersReceived(workers)
                }
            } catch (e: Exception) {
                withContext(dispatcherMain) {
                    _errorMessage.value = "Ошибка получения списка работников: ${e.message}"
                }
            }
        }
    }

    fun comeback() {
        navigationF.update(ScreenF.Pop)
        Log.d("NAVIGATION", "AddWorkerViewModel update to: ScreenF.Pop")
        clear.clearViewModel(AddWorkerViewModel::class.java)
    }

//    fun updateWorker(worker: Worker) {
//        workersRepository.updateWorker(worker)
//    }
//
//    fun deleteWorker(workerId: String) {
//        workersRepository.deleteWorker(workerId)
//    }

}