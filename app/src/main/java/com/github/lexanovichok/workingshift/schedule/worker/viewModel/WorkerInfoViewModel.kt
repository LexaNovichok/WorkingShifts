package com.github.lexanovichok.workingshift.schedule.worker.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.github.lexanovichok.workingshift.core.ClearViewModel
import com.github.lexanovichok.workingshift.schedule.main.NavigationF
import com.github.lexanovichok.workingshift.schedule.main.ScreenF
import com.github.lexanovichok.workingshift.schedule.userData.Worker
import com.github.lexanovichok.workingshift.schedule.worker.core.WorkerInfoLiveDataWrapper
import com.github.lexanovichok.workingshift.schedule.worker.model.WorkersRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WorkerInfoViewModel(
    private val navigationF: NavigationF.Mutable,
    private val workersRepository: WorkersRepository,
    private val workerInfoLiveDataWrapper: WorkerInfoLiveDataWrapper.Mutable,
    private val clear : ClearViewModel,
    private val dispatcher : CoroutineDispatcher = Dispatchers.IO,
    private val dispatcherMain : CoroutineDispatcher = Dispatchers.Main
) : ViewModel() {

    private val viewModelScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)


    fun updateWorker(worker: Worker) {
        Log.d("SCHEDULE", "WorkerInfoViewModel updateWorker id: ${worker.id}, name: ${worker.name}, contacts: ${worker.contacts}")
        viewModelScope.launch(dispatcher) {
            workersRepository.updateWorker(worker)
            withContext(dispatcherMain) {
                workerInfoLiveDataWrapper.update(worker)
            }
        }
        comeback()
    }

    fun deleteWorker(workerId: String) {
        viewModelScope.launch(dispatcher) {
            workersRepository.deleteWorker(workerId)
            withContext(dispatcherMain) {
                comeback()
            }
        }
    }

    fun workerInfoLiveData() : LiveData<Worker> = workerInfoLiveDataWrapper.liveData()


    private fun comeback() {
        navigationF.update(ScreenF.Pop)
        Log.d("NAVIGATION", "WorkerInfoViewModel update to: ScreenF.Pop")
        clear.clearViewModel(WorkerInfoViewModel::class.java)
    }
}