package com.github.lexanovichok.workingshifts.schedule.tasks.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.lexanovichok.workingshifts.core.AbstractFragment
import com.github.lexanovichok.workingshifts.core.ProvideViewModel
import com.github.lexanovichok.workingshifts.databinding.FragmentTaskInfoBinding
import com.github.lexanovichok.workingshifts.schedule.tasks.viewModel.TaskInfoViewModel
import com.github.lexanovichok.workingshifts.schedule.userData.Task

class TaskInfoFragment : AbstractFragment<FragmentTaskInfoBinding>() {

    private lateinit var viewModel : TaskInfoViewModel
    override fun bind(inflater: LayoutInflater, container: ViewGroup?): FragmentTaskInfoBinding =
        FragmentTaskInfoBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as ProvideViewModel).viewModel(TaskInfoViewModel::class.java)
        val currentTask : Task? = viewModel.taskInfoLiveData().value

        viewModel.taskInfoLiveData().observe(viewLifecycleOwner) { task ->
            Log.d("TaskInfoFragment", "task: workerName: ${task.worker.name}, address: ${task.address.city}")
            bindFields(task)
        }

        binding.saveButton.setOnClickListener {
            val editedTask = currentTask?.copy(description = binding.descriptionEditText.text.toString())
            editedTask?.let {
                viewModel.updateTask(editedTask)
            }
        }

        binding.deleteButton.setOnClickListener {
            viewModel.taskInfoLiveData().value?.let {
                hideKeyBoard()
                viewModel.deleteTask(it.id)
            }
        }

        binding.workerCard.setOnClickListener {
            currentTask?.let {
                viewModel.getWorkerById(currentTask.worker.id) //update workerInfoLiveDataWrapper (current worker)
                Log.d("SCHEDULE", "Worker: ${it.worker}")
                viewModel.workerInfoFragment()
            }
        }

        binding.addressCard.setOnClickListener {
            currentTask?.let {
                viewModel.getAddressById(currentTask.address.id)
                viewModel.addressInfoFragment()
            }
        }
    }

    private fun bindFields(task : Task?) = with(binding) {
        if (task != null) {
            workerName.text = task.worker.name
            workerContacts.text = task.worker.contacts
            cityTextView.text = task.address.city
            streetTextView.text = task.address.street
            descriptionEditText.setText(task.description)
        }
        else {
            with(binding) {
                workerName.text = ""
                workerContacts.text = ""
                cityTextView.text = ""
                streetTextView.text = ""
                descriptionEditText.setText("")
            }
        }
    }
}
