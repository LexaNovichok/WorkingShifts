package com.github.lexanovichok.workingshifts.authentication.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.addTextChangedListener
import com.github.lexanovichok.workingshifts.core.AbstractFragment
import com.github.lexanovichok.workingshifts.core.BundleWrapper
import com.github.lexanovichok.workingshifts.core.ProvideViewModel
import com.github.lexanovichok.workingshifts.databinding.FragmentLoginBinding
import com.github.lexanovichok.workingshifts.schedule.tasks.view.TasksFragment
import kotlin.system.exitProcess

class LoginFragment : AbstractFragment<FragmentLoginBinding>() {

    private lateinit var loginViewModel : LoginViewModel

    init {
        Log.d("LC", "LoginFragment init")
    }
    override fun bind(inflater: LayoutInflater, container: ViewGroup?): FragmentLoginBinding =
        FragmentLoginBinding.inflate(inflater, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("LC", "LoginFragment onViewCreated")
        loginViewModel = (activity as ProvideViewModel).viewModel(LoginViewModel::class.java)

        loginViewModel.init()

//        loginViewModel.isLoggedIn.observe(viewLifecycleOwner) {
//            if (it && loginViewModel.isEmailVerified()) {
//                loginViewModel.scheduleFragment()
//            }
//        }

        loginViewModel.errorMessage.observe(viewLifecycleOwner) {
            it?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }

        loginViewModel.isLoggedIn.observe(viewLifecycleOwner) { isLoggedIn ->
            if (isLoggedIn == true) {
                loginViewModel.isEmailVerified.observe(viewLifecycleOwner) { isVerified ->
                    if (isVerified == true) {
                        loginViewModel.mainFragment()
                        Log.d("LC", "LoginFragment init: update to MainFragmentScreenA")
                    } else {
                        Toast.makeText(requireContext(), "Please verify your email first", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        binding.loginButton.setOnClickListener {
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()

            hideKeyBoard()
            loginViewModel.login(email, password)
        }

        binding.signupTextView.setOnClickListener {
            loginViewModel.registerFragment()
        }

        binding.forgotPasswordTextView.setOnClickListener {
            loginViewModel.passwordResetScreen()
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().finish()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        loginViewModel.resetError()
    }


}