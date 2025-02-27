package com.github.lexanovichok.workingshifts.core

import android.app.Application
import androidx.lifecycle.ViewModel

class App : Application(), ProvideViewModel {

    private lateinit var factory: ViewModelFactory

    private val clear : ClearViewModel = object : ClearViewModel {
        override fun clearViewModel(viewModelClass: Class<out ViewModel>) {
            factory.clearViewModel(viewModelClass)
        }
    }

    override fun onCreate() {
        super.onCreate()
        val provideViewModel = ProvideViewModel.Base(clear)
        factory = ViewModelFactory.Base(provideViewModel)
    }

    override fun <T : ViewModel> viewModel(viewModelClass: Class<T>): T =
        factory.viewModel(viewModelClass)

}