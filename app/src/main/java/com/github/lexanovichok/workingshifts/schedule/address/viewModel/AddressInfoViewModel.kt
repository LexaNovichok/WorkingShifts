package com.github.lexanovichok.workingshifts.schedule.address.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.github.lexanovichok.workingshifts.core.ClearViewModel
import com.github.lexanovichok.workingshifts.schedule.address.core.AddressInfoLiveDataWrapper
import com.github.lexanovichok.workingshifts.schedule.address.model.AddressRepository
import com.github.lexanovichok.workingshifts.schedule.main.NavigationF
import com.github.lexanovichok.workingshifts.schedule.main.ScreenF
import com.github.lexanovichok.workingshifts.schedule.userData.Address
import com.github.lexanovichok.workingshifts.schedule.worker.viewModel.WorkerInfoViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddressInfoViewModel(
    private val navigationF: NavigationF.Mutable,
    private val addressRepository: AddressRepository,
    private val addressInfoLiveDataWrapper: AddressInfoLiveDataWrapper.Mutable,
    private val clear : ClearViewModel,
    private val dispatcher : CoroutineDispatcher = Dispatchers.IO,
    private val dispatcherMain : CoroutineDispatcher = Dispatchers.Main
) : ViewModel() {

    private val viewModelScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    fun updateAddress(address: Address) {
        Log.d("SCHEDULE", "WorkerInfoViewModel updateWorker id: ${address.id}, city: ${address.city}, street: ${address.street}")
        viewModelScope.launch(dispatcher) {
            addressRepository.updateAddress(address)
            withContext(dispatcherMain) {
                addressInfoLiveDataWrapper.update(address)
            }
        }
        comeback()
    }

    fun deleteAddress(addressId : String) {
        viewModelScope.launch(dispatcher) {
            addressRepository.deleteAddress(addressId)
            withContext(dispatcherMain) {
                comeback()
            }
        }
    }

    fun addressInfoLiveData() : LiveData<Address> = addressInfoLiveDataWrapper.liveData()

    private fun comeback() {
        navigationF.update(ScreenF.Pop)
        Log.d("NAVIGATION", "AddressInfoViewModel update to: Screen.Pop")
        clear.clearViewModel(WorkerInfoViewModel::class.java)
    }
}