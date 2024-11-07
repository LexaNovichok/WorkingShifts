package com.github.lexanovichok.workingshifts.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.github.lexanovichok.workingshifts.core.AbstractFragment
import com.github.lexanovichok.workingshifts.databinding.FragmentSignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterFragment : AbstractFragment<FragmentSignupBinding>() {

    private lateinit var registerViewModel: RegisterViewModel
    private lateinit var auth: FirebaseAuth
    override fun bind(inflater: LayoutInflater, container: ViewGroup?): FragmentSignupBinding =
        FragmentSignupBinding.inflate(inflater, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth


        registerViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }

        registerViewModel.isLoggedIn.observe(viewLifecycleOwner) { isLoggedIn ->
            if (isLoggedIn) {
                // Переход на следующий экран или сообщение об успешной регистрации
                Toast.makeText(requireContext(), "Registration successful", Toast.LENGTH_SHORT)
                    .show()
            }


        }

        binding.signupButton.setOnClickListener {
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()
            val passwordConfirm = binding.passwordConfirm.toString()

            registerViewModel.register(email, password, passwordConfirm)
        }
    }

}
