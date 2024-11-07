package com.github.lexanovichok.workingshifts.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.github.lexanovichok.workingshifts.core.AbstractFragment
import com.github.lexanovichok.workingshifts.core.ProvideViewModel
import com.github.lexanovichok.workingshifts.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AuthFragment : AbstractFragment<FragmentLoginBinding>() {

    private lateinit var authViewModel : AuthViewModel
    private lateinit var auth: FirebaseAuth
    override fun bind(inflater: LayoutInflater, container: ViewGroup?): FragmentLoginBinding =
        FragmentLoginBinding.inflate(inflater, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authViewModel = (activity as ProvideViewModel).viewModel(AuthViewModel::class.java)

        auth = Firebase.auth




    }



}