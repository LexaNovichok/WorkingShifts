package com.github.lexanovichok.workingshifts.authentication.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.github.lexanovichok.workingshifts.core.AbstractFragment
import com.github.lexanovichok.workingshifts.core.ProvideViewModel
import com.github.lexanovichok.workingshifts.databinding.FragmentSignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterFragment : AbstractFragment<FragmentSignupBinding>() {

    private lateinit var registerViewModel: RegisterViewModel
    override fun bind(inflater: LayoutInflater, container: ViewGroup?): FragmentSignupBinding =
        FragmentSignupBinding.inflate(inflater, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registerViewModel = (activity as ProvideViewModel).viewModel(RegisterViewModel::class.java)

        registerViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }

        binding.signupButton.setOnClickListener {
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()
            val passwordConfirm = binding.passwordConfirm.text.toString()

            hideKeyBoard()
            registerViewModel.register(email, password, passwordConfirm)
        }

        binding.logInTextView.setOnClickListener {
            registerViewModel.loginFragment()
        }

        binding.resendEmailTextView.setOnClickListener {
            registerViewModel.resendVerificationEmail()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        registerViewModel.resetError()
    }

}
