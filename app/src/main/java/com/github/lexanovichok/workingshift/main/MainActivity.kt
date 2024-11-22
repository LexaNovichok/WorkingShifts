package com.github.lexanovichok.workingshift.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import com.github.lexanovichok.workingshift.R
import com.github.lexanovichok.workingshift.core.ProvideViewModel
import com.github.lexanovichok.workingshift.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), ProvideViewModel {

    private lateinit var binding : ActivityMainBinding
    private lateinit var mainViewModel : MainViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("LC", "MainActivity onCreate")

        mainViewModel = viewModel(MainViewModel::class.java)

        mainViewModel.liveData().observe(this) { screen ->
            Log.d("LC", "MainActivity liveData changed screen: $screen")
//            if (!mainViewModel.isLoggedIn()) {
//                screen.show(supportFragmentManager, binding.container.id)
//                //Log.d("NAVIGATION", "MainActivity livedata screen: $screen")
//            }
            screen.show(supportFragmentManager, binding.container.id)
        }

        mainViewModel.init(savedInstanceState == null)

    }

    override fun <T : ViewModel> viewModel(viewModelClass: Class<T>): T {
        return (application as ProvideViewModel).viewModel(viewModelClass)
    }


}