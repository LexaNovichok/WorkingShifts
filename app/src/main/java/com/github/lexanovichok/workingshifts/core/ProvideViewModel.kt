package com.github.lexanovichok.workingshifts.core

import com.github.lexanovichok.workingshifts.authentication.auth.AuthViewModel
import androidx.lifecycle.ViewModel
import com.github.lexanovichok.workingshifts.authentication.auth.AuthRepository
import com.github.lexanovichok.workingshifts.authentication.login.LoginViewModel
import com.github.lexanovichok.workingshifts.main.MainViewModel
import com.github.lexanovichok.workingshifts.main.NavigationA
import com.github.lexanovichok.workingshifts.authentication.register.RegisterViewModel
import com.github.lexanovichok.workingshifts.authentication.resetPassword.PasswordResetViewModel
import com.github.lexanovichok.workingshifts.schedule.main.MainFragmentViewModel
import com.github.lexanovichok.workingshifts.schedule.main.NavigationF
import com.github.lexanovichok.workingshifts.schedule.tasks.model.TaskRepository
import com.github.lexanovichok.workingshifts.schedule.tasks.viewModel.TaskDayViewModel
import com.github.lexanovichok.workingshifts.schedule.tasks.viewModel.TasksViewModel
import com.github.lexanovichok.workingshifts.schedule.worker.core.WorkerInfoLiveDataWrapper
import com.github.lexanovichok.workingshifts.schedule.worker.core.WorkersListLiveDataWrapper
import com.github.lexanovichok.workingshifts.schedule.worker.model.WorkersRepository
import com.github.lexanovichok.workingshifts.schedule.worker.viewModel.AddWorkerViewModel
import com.github.lexanovichok.workingshifts.schedule.worker.viewModel.WorkerInfoViewModel
import com.github.lexanovichok.workingshifts.schedule.worker.viewModel.WorkersViewModel
import java.lang.IllegalStateException

interface ProvideViewModel {

    fun <T : ViewModel> viewModel(viewModelClass: Class<T>) : T

    class Base(
        private val clearViewModel : ClearViewModel
    ) : ProvideViewModel {
        private val authRepository = AuthRepository()
        private val inputValidator  = InputValidator()
        private val navigationA = NavigationA.Base()

        private val taskRepository = TaskRepository()
        private val workerRepository = WorkersRepository()
        private val workersListLiveDataWrapper = WorkersListLiveDataWrapper.Base()

        private val navigationF = NavigationF.Base()
        private val workerInfoLiveDataWrapper = WorkerInfoLiveDataWrapper.Base()

        override fun <T : ViewModel> viewModel(viewModelClass: Class<T>): T {
            return when(viewModelClass) {
                MainViewModel::class.java -> MainViewModel(navigationA)
                AuthViewModel::class.java -> AuthViewModel(authRepository, inputValidator)
                LoginViewModel::class.java -> LoginViewModel(navigationA, authRepository, inputValidator)
                RegisterViewModel::class.java -> RegisterViewModel(navigationA, authRepository, inputValidator)
                PasswordResetViewModel::class.java -> PasswordResetViewModel(navigationA, clearViewModel, authRepository, inputValidator)

                MainFragmentViewModel::class.java -> MainFragmentViewModel(navigationF)
                TasksViewModel::class.java -> TasksViewModel(navigationF, taskRepository)
                TaskDayViewModel::class.java -> TaskDayViewModel(taskRepository)
                WorkersViewModel::class.java -> WorkersViewModel(navigationF, clearViewModel, workerRepository, workerInfoLiveDataWrapper, workersListLiveDataWrapper)
                AddWorkerViewModel::class.java -> AddWorkerViewModel(navigationF, clearViewModel, workerRepository, workersListLiveDataWrapper)
                WorkerInfoViewModel::class.java -> WorkerInfoViewModel(navigationF, workerRepository, workerInfoLiveDataWrapper, clearViewModel)

                else -> throw IllegalStateException("unknown viewModelClass $viewModelClass")
            } as T
        }

    }
}