package com.github.lexanovichok.workingshifts.core

import android.app.Application
import androidx.lifecycle.ViewModel

class App : Application(), ProvideViewModel {

    private lateinit var factory: ViewModelFactory

    override fun onCreate() {
        super.onCreate()
        val provideViewModel = ProvideViewModel.Base()
        factory = ViewModelFactory.Base(provideViewModel)
    }

    override fun <T : ViewModel> viewModel(viewModelClass: Class<T>): T =
        factory.viewModel(viewModelClass)

}