package com.github.lexanovichok.workingshifts.schedule.address.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.github.lexanovichok.workingshifts.core.ClearViewModel
import com.github.lexanovichok.workingshifts.schedule.address.core.AddAddressesScreenF
import com.github.lexanovichok.workingshifts.schedule.address.core.AddressInfoLiveDataWrapper
import com.github.lexanovichok.workingshifts.schedule.address.core.AddressInfoScreenF
import com.github.lexanovichok.workingshifts.schedule.address.core.AddressesListLiveDataWrapper
import com.github.lexanovichok.workingshifts.schedule.address.model.AddressRepository
import com.github.lexanovichok.workingshifts.schedule.main.NavigationF
import com.github.lexanovichok.workingshifts.schedule.userData.Address
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddressesViewModel(
    private val navigationF: NavigationF.Update,
    private val clear: ClearViewModel,
    private val addressRepository: AddressRepository,
    private val addressesListLiveDataWrapper: AddressesListLiveDataWrapper.Mutable,
    private val addressInfoLiveDataWrapper: AddressInfoLiveDataWrapper.Mutable,
    private val dispatcher : CoroutineDispatcher = Dispatchers.IO,
    private val dispatcherMain : CoroutineDispatcher = Dispatchers.Main
) : ViewModel() {

    init {
        Log.d("LC", "AddressesViewModel init")
    }

    private val viewModelScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    init {
        addressRepository.addressesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                viewModelScope.launch(dispatcher) {
                    val list = addressRepository.getAddresses()
                    withContext(dispatcherMain) {
                        addressesListLiveDataWrapper.update(list)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("SCHEDULE", "AddressesViewModel ${error.message}")
            }

        })
    }

    fun deleteAddress(addressId : String) {
        viewModelScope.launch(dispatcher) {
            addressRepository.deleteAddress(addressId)
        }
    }

    fun addressesListLiveData() : LiveData<List<Address>> = addressesListLiveDataWrapper.liveData()

    fun updateAddressFromRcView(address: Address) {
        addressInfoLiveDataWrapper.update(address) //адрес из айтема ресайклера по которому кликнули
    }

    fun addAddressFragment() {
        navigationF.update(AddAddressesScreenF)
        Log.d("NAVIGATION", "AddressesViewModel update to: AddAddressesScreenF")
    }

    fun addressInfoFragment() {
        navigationF.update(AddressInfoScreenF)
        Log.d("NAVIGATION", "AddressesViewModel update to: AddressInfoScreenF")
    }
}