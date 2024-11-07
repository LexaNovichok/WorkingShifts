package com.github.lexanovichok.workingshifts

import android.content.Intent
import com.github.lexanovichok.workingshifts.auth.AuthViewModel
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.github.lexanovichok.workingshifts.auth.AuthFragment
import com.github.lexanovichok.workingshifts.core.ProvideViewModel
import com.github.lexanovichok.workingshifts.databinding.ActivityMainBinding
import com.github.lexanovichok.workingshifts.main.MainViewModel
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity(), ProvideViewModel {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, AuthFragment())
            .addToBackStack("AuthFragment")
            .commit()

    }


    override fun <T : ViewModel> viewModel(viewModelClass: Class<T>): T {
        return (application as ProvideViewModel).viewModel(viewModelClass)
    }
}