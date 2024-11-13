package com.github.lexanovichok.workingshifts.schedule.worker.viewModel

import androidx.lifecycle.ViewModel
import com.github.lexanovichok.workingshifts.core.ClearViewModel
import com.github.lexanovichok.workingshifts.main.ScreenA
import com.github.lexanovichok.workingshifts.schedule.main.NavigationF
import com.github.lexanovichok.workingshifts.schedule.main.ScreenF
import com.github.lexanovichok.workingshifts.schedule.userData.Worker
import com.github.lexanovichok.workingshifts.schedule.worker.AddWorkerScreenF
import com.github.lexanovichok.workingshifts.schedule.worker.model.WorkersRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class WorkersViewModel(
    private val navigationF: NavigationF.Update,
    private val clear: ClearViewModel,
    private val workersRepository: WorkersRepository,
    private val dispatcher : CoroutineDispatcher = Dispatchers.IO,
    private val dispatcherMain : CoroutineDispatcher = Dispatchers.Main
) : ViewModel() {

    private val viewModelScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    fun getWorkers(onWorkersReceived: (List<Worker>) -> Unit) {
        viewModelScope.launch(dispatcher) {
            workersRepository.getWorkers()
        }
    }

//    fun updateWorker(worker: Worker) {
//        workersRepository.updateWorker(worker)
//    }
//
//    fun deleteWorker(workerId: String) {
//        workersRepository.deleteWorker(workerId)
//    }

    fun addWorkerFragment() {
        navigationF.update(AddWorkerScreenF)
    }
    fun comeback() {
        navigationF.update(ScreenF.Pop)
        clear.clearViewModel(AddWorkerViewModel::class.java)
    }
}