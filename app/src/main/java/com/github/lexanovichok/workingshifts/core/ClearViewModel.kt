package com.github.lexanovichok.workingshifts.core

import androidx.lifecycle.ViewModel

interface ClearViewModel {
    fun clearViewModel(viewModelClass : Class<out ViewModel>)
}