package com.github.lexanovichok.workingshifts.schedule.worker.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.github.lexanovichok.workingshifts.core.ClearViewModel
import com.github.lexanovichok.workingshifts.schedule.main.NavigationF
import com.github.lexanovichok.workingshifts.schedule.main.ScreenF
import com.github.lexanovichok.workingshifts.schedule.userData.Worker
import com.github.lexanovichok.workingshifts.schedule.worker.core.AddWorkerScreenF
import com.github.lexanovichok.workingshifts.schedule.worker.core.WorkerInfoLiveDataWrapper
import com.github.lexanovichok.workingshifts.schedule.worker.core.WorkerInfoScreenF
import com.github.lexanovichok.workingshifts.schedule.worker.core.WorkersListLiveDataWrapper
import com.github.lexanovichok.workingshifts.schedule.worker.model.WorkersRepository
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WorkersViewModel(
    private val navigationF: NavigationF.Update,
    private val clear: ClearViewModel,
    private val workersRepository: WorkersRepository,
    private val workersInfoLiveDataWrapper: WorkerInfoLiveDataWrapper.Mutable,
    private val workersListLiveDataWrapper: WorkersListLiveDataWrapper.Mutable,
    private val dispatcher : CoroutineDispatcher = Dispatchers.IO,
    private val dispatcherMain : CoroutineDispatcher = Dispatchers.Main
) : ViewModel() {

    init {
        Log.d("LC", "WorkersViewModel: init")
    }
    private val viewModelScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    init {
        workersRepository.workersRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("SCHEDULE", "DATA IN DB HAS BEEN CHANGED")
                viewModelScope.launch(dispatcher) {
                    val list = workersRepository.getWorkers()
                    withContext(dispatcherMain) {
                        workersListLiveDataWrapper.update(list)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("SCHEDULE", "WorkersViewModel ${error.message}")
            }
        })
    }

    fun getWorkers() {
        viewModelScope.launch(dispatcher) {
            val list = workersRepository.getWorkers()
            val size = list.size
            Log.d("SCHEDULE", "WorkersViewModel getWorkers. Name: ${list[size-1].name}, contacts: ${list[size-1].contacts}")

            withContext(dispatcherMain) {
                workersListLiveDataWrapper.update(list)
            }
        }
    }

    fun deleteWorker(workerId: String) {
        viewModelScope.launch(dispatcher) {
            workersRepository.deleteWorker(workerId)
        }
    }

    fun workersListLiveData(): LiveData<List<Worker>> = workersListLiveDataWrapper.liveData()

    fun workerInfoLiveData() : LiveData<Worker> = workersInfoLiveDataWrapper.liveData()

    fun updateCurrentWorkerFromRcView(worker: Worker) {
        workersInfoLiveDataWrapper.update(worker)
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

    fun workerInfoFragment() {
        navigationF.update(WorkerInfoScreenF)
    }

    fun comeback() {
        navigationF.update(ScreenF.Pop)
        clear.clearViewModel(AddWorkerViewModel::class.java)
    }


}