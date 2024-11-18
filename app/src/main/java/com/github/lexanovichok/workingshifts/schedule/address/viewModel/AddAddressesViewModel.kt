package com.github.lexanovichok.workingshifts.schedule.address.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.lexanovichok.workingshifts.core.ClearViewModel
import com.github.lexanovichok.workingshifts.schedule.address.model.AddressRepository
import com.github.lexanovichok.workingshifts.schedule.main.NavigationF
import com.github.lexanovichok.workingshifts.schedule.main.ScreenF
import com.github.lexanovichok.workingshifts.schedule.userData.Address
import com.github.lexanovichok.workingshifts.schedule.userData.Worker
import com.github.lexanovichok.workingshifts.schedule.worker.viewModel.AddWorkerViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddAddressesViewModel(
    private val navigationF: NavigationF.Update,
    private val clear: ClearViewModel,
    private val addressRepository: AddressRepository,
    private val dispatcher : CoroutineDispatcher = Dispatchers.IO,
    private val dispatcherMain : CoroutineDispatcher = Dispatchers.Main
) : ViewModel() {
    private val viewModelScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    private val _errorMessage = MutableLiveData<String?>()  // Для хранения ошибок
    val errorMessage : LiveData<String?> get() = _errorMessage

    fun addAddress(address: Address) {
        viewModelScope.launch(dispatcher) {
            try {
                addressRepository.addAddress(address)
            } catch (e: Exception) {
                withContext(dispatcherMain) {
                    _errorMessage.value = "Ошибка добавления работника: ${e.message}"
                }
            }
        }
    }

    fun comeback() {
        navigationF.update(ScreenF.Pop)
        Log.d("NAVIGATION", "AddAddressesViewModel update to: ScreenF.Pop")
        clear.clearViewModel(AddAddressesViewModel::class.java)
    }
}