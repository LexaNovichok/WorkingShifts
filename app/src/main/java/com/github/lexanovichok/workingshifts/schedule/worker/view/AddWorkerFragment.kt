package com.github.lexanovichok.workingshifts.schedule.worker.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.github.lexanovichok.workingshifts.core.AbstractFragment
import com.github.lexanovichok.workingshifts.core.ProvideViewModel
import com.github.lexanovichok.workingshifts.databinding.FragmentAddWorkerBinding
import com.github.lexanovichok.workingshifts.schedule.userData.Worker
import com.github.lexanovichok.workingshifts.schedule.worker.viewModel.AddWorkerViewModel

class AddWorkerFragment : AbstractFragment<FragmentAddWorkerBinding>() {

    private lateinit var addWorkerViewModel: AddWorkerViewModel

    override fun bind(inflater: LayoutInflater, container: ViewGroup?): FragmentAddWorkerBinding =
        FragmentAddWorkerBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addWorkerViewModel = (activity as ProvideViewModel).viewModel(AddWorkerViewModel::class.java)


        binding.saveButton.setOnClickListener {
            with (binding) {
                val name = nameEditText.text.toString()
                val contactInfo = contactsEditText.text.toString()
                val description = descriptionEditText.text.toString()

                if (name.trim().isNotBlank()) {
                    addWorkerViewModel.addWorker(Worker(name=name, contacts = contactInfo, description = description))

                    hideKeyBoard()
                    addWorkerViewModel.comeback()
                }
                else {
                    Toast.makeText(activity, "Name can't be empty", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }
}