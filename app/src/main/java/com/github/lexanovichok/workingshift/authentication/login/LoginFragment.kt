package com.github.lexanovichok.workingshift.authentication.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.lifecycleScope
import com.github.lexanovichok.workingshift.databinding.FragmentLoginBinding
import com.github.lexanovichok.workingshift.core.AbstractFragment
import com.github.lexanovichok.workingshift.core.ProvideViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

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


        binding.loginButton.setOnClickListener {
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()

            hideKeyBoard()


            lifecycleScope.launch {
                loginViewModel.login(email, password)

                val userState = loginViewModel.checkUserStatus() // Ждём завершения

                Log.d("LC", "isLoggedIn: ${userState.isLoggedIn}, isVerified: ${userState.isEmailVerified} isAdmin: ${userState.isAdmin}")

                if (userState.isLoggedIn && userState.isEmailVerified) {
                    if (userState.isAdmin) {
                        loginViewModel.mainFragment()
                    } else {
                        Toast.makeText(requireContext(), "Недостаточно прав", Toast.LENGTH_SHORT).show()
                    }
                } else if (userState.isLoggedIn && !userState.isEmailVerified) {
                    //Toast.makeText(requireContext(), "Please verify your email first", Toast.LENGTH_SHORT).show()
                }
            }


//            loginViewModel.userState.observe(viewLifecycleOwner) { userState ->
//                Log.d("LC", "isLoggedIn: ${userState.isLoggedIn}, isVerified: ${userState.isEmailVerified} isAdmin: ${userState.isAdmin}")
//                if (userState.isLoggedIn && userState.isEmailVerified) {
//                    if (userState.isAdmin) {
//                        loginViewModel.mainFragment()
//                    } else {
//                        Toast.makeText(requireContext(), "Недостаточно прав", Toast.LENGTH_SHORT).show()
//                    }
//                } else if (userState.isLoggedIn && !userState.isEmailVerified) {
//                    Toast.makeText(requireContext(), "Please verify your email first", Toast.LENGTH_SHORT).show()
//                }
//            }
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

    companion object {
        fun newInstance(): LoginFragment {
            return LoginFragment().apply {
                arguments = Bundle().apply {
                    // Добавьте данные, если нужно
                }
            }
        }
    }


}