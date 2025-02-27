package com.github.lexanovichok.workingshifts.authentication.resetPassword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.github.lexanovichok.workingshifts.core.AbstractFragment
import com.github.lexanovichok.workingshifts.core.ProvideViewModel
import com.github.lexanovichok.workingshifts.databinding.FragmentPasswordResetBinding

class PasswordResetFragment : AbstractFragment<FragmentPasswordResetBinding>() {

    private lateinit var viewModel : PasswordResetViewModel
    override fun bind(inflater: LayoutInflater, container: ViewGroup?): FragmentPasswordResetBinding =
        FragmentPasswordResetBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as ProvideViewModel).viewModel(PasswordResetViewModel::class.java)

        viewModel.passwordResetSuccess.observe(viewLifecycleOwner) { result ->
            if (result)
                Toast.makeText(requireContext(), "Password reset email sent!", Toast.LENGTH_SHORT).show()
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }

        binding.resetPasswordButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()

            hideKeyBoard()
            viewModel.resetPassword(email)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.resetError()
    }
}