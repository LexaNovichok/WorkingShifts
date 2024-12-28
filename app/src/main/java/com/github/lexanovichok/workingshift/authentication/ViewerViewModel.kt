package com.github.lexanovichok.workingshift.authentication

import androidx.lifecycle.ViewModel
import com.github.lexanovichok.workingshift.authentication.auth.AuthRepository
import com.github.lexanovichok.workingshift.schedule.address.core.AddressesListLiveDataWrapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ViewerViewModel(
    private val authRepository: AuthRepository,
    private val viewerLiveDataWrapper: ViewerLiveDataWrapper.Mutable,
    private val adminLiveDataWrapper: AdminLiveDataWrapper.Mutable,
    private val dispatcher : CoroutineDispatcher = Dispatchers.IO,
    private val dispatcherMain : CoroutineDispatcher = Dispatchers.Main
) : ViewModel() {

    private val viewModelScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

     init {
        viewModelScope.launch {
            val isViewer = authRepository.isViewer()
            val isAdmin = authRepository.isAdmin()
            withContext(dispatcherMain) {
                viewerLiveDataWrapper.update(isViewer)
                adminLiveDataWrapper.update(isAdmin)
            }
        }
     }

    fun viewerLiveData() = viewerLiveDataWrapper.liveData()
    fun adminLiveData() = adminLiveDataWrapper.liveData()

}