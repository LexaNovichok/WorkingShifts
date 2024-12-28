package com.github.lexanovichok.workingshift.core

import com.github.lexanovichok.workingshift.authentication.auth.AuthViewModel
import androidx.lifecycle.ViewModel
import com.github.lexanovichok.workingshift.authentication.AdminLiveDataWrapper
import com.github.lexanovichok.workingshift.authentication.LoggedInLiveDataWrapper
import com.github.lexanovichok.workingshift.authentication.ViewerLiveDataWrapper
import com.github.lexanovichok.workingshift.authentication.ViewerViewModel
import com.github.lexanovichok.workingshift.authentication.auth.AuthRepository
import com.github.lexanovichok.workingshift.authentication.login.LoginViewModel
import com.github.lexanovichok.workingshift.main.MainViewModel
import com.github.lexanovichok.workingshift.main.NavigationA
import com.github.lexanovichok.workingshift.authentication.register.RegisterViewModel
import com.github.lexanovichok.workingshift.authentication.resetPassword.PasswordResetViewModel
import com.github.lexanovichok.workingshift.schedule.address.core.AddressInfoLiveDataWrapper
import com.github.lexanovichok.workingshift.schedule.address.core.AddressesListLiveDataWrapper
import com.github.lexanovichok.workingshift.schedule.address.model.AddressRepository
import com.github.lexanovichok.workingshift.schedule.address.viewModel.AddAddressesViewModel
import com.github.lexanovichok.workingshift.schedule.address.viewModel.AddressInfoViewModel
import com.github.lexanovichok.workingshift.schedule.address.viewModel.AddressesViewModel
import com.github.lexanovichok.workingshift.schedule.main.MainFragmentViewModel
import com.github.lexanovichok.workingshift.schedule.main.NavigationF
import com.github.lexanovichok.workingshift.schedule.tasks.core.TaskInfoLiveDataWrapper
import com.github.lexanovichok.workingshift.schedule.tasks.core.TaskListLiveDataWrapper
import com.github.lexanovichok.workingshift.schedule.tasks.core.TasksHistoryLiveDataWrapper
import com.github.lexanovichok.workingshift.schedule.tasks.model.TaskRepository
import com.github.lexanovichok.workingshift.schedule.tasks.viewModel.TaskAddViewModel
import com.github.lexanovichok.workingshift.schedule.tasks.viewModel.TaskDayViewModel
import com.github.lexanovichok.workingshift.schedule.tasks.viewModel.TaskHistoryViewModel
import com.github.lexanovichok.workingshift.schedule.tasks.viewModel.TaskInfoViewModel
import com.github.lexanovichok.workingshift.schedule.worker.core.WorkerInfoLiveDataWrapper
import com.github.lexanovichok.workingshift.schedule.worker.core.WorkersListLiveDataWrapper
import com.github.lexanovichok.workingshift.schedule.worker.model.WorkersRepository
import com.github.lexanovichok.workingshift.schedule.worker.viewModel.AddWorkerViewModel
import com.github.lexanovichok.workingshift.schedule.worker.viewModel.WorkerInfoViewModel
import com.github.lexanovichok.workingshift.schedule.worker.viewModel.WorkersViewModel
import java.lang.IllegalStateException

interface ProvideViewModel {

    fun <T : ViewModel> viewModel(viewModelClass: Class<T>) : T

    class Base(
        private val clearViewModel : ClearViewModel
    ) : ProvideViewModel {
        private val authRepository = AuthRepository()
        private val inputValidator  = InputValidator()
        private val navigationA = NavigationA.Base()
        private val navigationF = NavigationF.Base()

        private val taskRepository = TaskRepository()
        private val taskListLiveDataWrapper = TaskListLiveDataWrapper.Base()
        private val taskInfoLiveDataWrapper = TaskInfoLiveDataWrapper.Base()
        private val tasksHistoryLiveDataWrapper = TasksHistoryLiveDataWrapper.Base()

        private val workerRepository = WorkersRepository()
        private val workersListLiveDataWrapper = WorkersListLiveDataWrapper.Base()
        private val workerInfoLiveDataWrapper = WorkerInfoLiveDataWrapper.Base()

        private val addressesRepository = AddressRepository()
        private val addressesListLiveDataWrapper = AddressesListLiveDataWrapper.Base()
        private val addressInfoLiveDataWrapper = AddressInfoLiveDataWrapper.Base()

        private val loggedInLiveDataWrapper = LoggedInLiveDataWrapper.Base()

        private val viewerLiveDataWrapper = ViewerLiveDataWrapper.Base()
        private val adminLiveDataWrapper = AdminLiveDataWrapper.Base()

        override fun <T : ViewModel> viewModel(viewModelClass: Class<T>): T {
            return when(viewModelClass) {
                MainViewModel::class.java -> MainViewModel(navigationA, loggedInLiveDataWrapper)
                AuthViewModel::class.java -> AuthViewModel(authRepository, inputValidator)
                LoginViewModel::class.java -> LoginViewModel(navigationA, loggedInLiveDataWrapper, viewerLiveDataWrapper, adminLiveDataWrapper, authRepository, inputValidator)
                RegisterViewModel::class.java -> RegisterViewModel(navigationA, authRepository, inputValidator)
                PasswordResetViewModel::class.java -> PasswordResetViewModel(navigationA, clearViewModel, authRepository, inputValidator)

                MainFragmentViewModel::class.java -> MainFragmentViewModel(navigationF, navigationA, authRepository, clearViewModel)

                TaskDayViewModel::class.java -> TaskDayViewModel(navigationF ,taskRepository, taskListLiveDataWrapper, taskInfoLiveDataWrapper)
                TaskAddViewModel::class.java -> TaskAddViewModel(navigationF, clearViewModel, workerRepository, workersListLiveDataWrapper, addressesRepository, addressesListLiveDataWrapper,taskRepository, taskListLiveDataWrapper)
                TaskInfoViewModel::class.java -> TaskInfoViewModel(navigationF, clearViewModel, workerRepository, workerInfoLiveDataWrapper, addressesRepository, addressInfoLiveDataWrapper,taskRepository, taskListLiveDataWrapper, taskInfoLiveDataWrapper)
                TaskHistoryViewModel::class.java -> TaskHistoryViewModel(navigationF, taskRepository, tasksHistoryLiveDataWrapper)

                WorkersViewModel::class.java -> WorkersViewModel(navigationF, clearViewModel, workerRepository, workerInfoLiveDataWrapper, workersListLiveDataWrapper)
                AddWorkerViewModel::class.java -> AddWorkerViewModel(navigationF, clearViewModel, workerRepository)
                WorkerInfoViewModel::class.java -> WorkerInfoViewModel(navigationF, workerRepository, workerInfoLiveDataWrapper, clearViewModel)

                AddressesViewModel::class.java -> AddressesViewModel(navigationF,clearViewModel, addressesRepository, addressesListLiveDataWrapper ,addressInfoLiveDataWrapper)
                AddAddressesViewModel::class.java -> AddAddressesViewModel(navigationF, clearViewModel, addressesRepository)
                AddressInfoViewModel::class.java -> AddressInfoViewModel(navigationF, addressesRepository, addressInfoLiveDataWrapper, clearViewModel)

                ViewerViewModel::class.java -> ViewerViewModel(authRepository, viewerLiveDataWrapper, adminLiveDataWrapper)

                else -> throw IllegalStateException("unknown viewModelClass $viewModelClass")
            } as T
        }

    }
}