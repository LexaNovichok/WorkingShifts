package com.github.lexanovichok.workingshifts.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModel
import com.github.lexanovichok.workingshifts.R
import com.github.lexanovichok.workingshifts.core.ProvideViewModel
import com.github.lexanovichok.workingshifts.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(), ProvideViewModel {

    private lateinit var binding : ActivityMainBinding
    private lateinit var mainViewModel : MainViewModel

    init {
        Log.d("LC", "MainActivity init")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("LC", "MainActivity onCreate")

        mainViewModel = viewModel(MainViewModel::class.java)

        mainViewModel.liveData().observe(this) { screen ->
            if (!mainViewModel.isLoggedIn()) {
                screen.show(supportFragmentManager, binding.container.id)
            }
        }

        mainViewModel.init(savedInstanceState == null)

    }


    override fun <T : ViewModel> viewModel(viewModelClass: Class<T>): T {
        return (application as ProvideViewModel).viewModel(viewModelClass)
    }
}