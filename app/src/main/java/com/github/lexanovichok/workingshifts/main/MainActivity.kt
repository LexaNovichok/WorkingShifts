package com.github.lexanovichok.workingshifts.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModel
import com.github.lexanovichok.workingshifts.R
import com.github.lexanovichok.workingshifts.auth.AuthFragment
import com.github.lexanovichok.workingshifts.core.ProvideViewModel
import com.github.lexanovichok.workingshifts.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), ProvideViewModel {

    private lateinit var binding : ActivityMainBinding
    private lateinit var mainViewModel : MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel = viewModel(MainViewModel::class.java)

        mainViewModel.liveData().observe(this) { screen ->
            screen.show(supportFragmentManager, binding.container.id)
        }

        mainViewModel.init(savedInstanceState == null)

    }


    override fun <T : ViewModel> viewModel(viewModelClass: Class<T>): T {
        return (application as ProvideViewModel).viewModel(viewModelClass)
    }
}