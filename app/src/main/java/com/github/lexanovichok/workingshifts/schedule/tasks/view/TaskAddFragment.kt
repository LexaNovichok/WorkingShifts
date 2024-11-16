package com.github.lexanovichok.workingshifts.schedule.tasks.view

import android.R
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import com.github.lexanovichok.workingshifts.core.AbstractFragment
import com.github.lexanovichok.workingshifts.core.ProvideViewModel
import com.github.lexanovichok.workingshifts.databinding.FragmentAddTaskBinding
import com.github.lexanovichok.workingshifts.schedule.tasks.viewModel.TaskAddViewModel
import com.github.lexanovichok.workingshifts.schedule.tasks.viewModel.TaskDayViewModel
import com.github.lexanovichok.workingshifts.schedule.tasks.viewModel.TasksViewModel
import com.github.lexanovichok.workingshifts.schedule.userData.Task


class TaskAddFragment : AbstractFragment<FragmentAddTaskBinding>() {

    private lateinit var taskAddViewModel : TaskAddViewModel
    private lateinit var taskDayViewModel: TaskDayViewModel
    override fun bind(inflater: LayoutInflater, container: ViewGroup?): FragmentAddTaskBinding =
        FragmentAddTaskBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        taskAddViewModel = (activity as ProvideViewModel).viewModel(TaskAddViewModel::class.java)
        taskDayViewModel = (activity as ProvideViewModel).viewModel(TaskDayViewModel::class.java)

        taskAddViewModel.getWorkers()
        taskAddViewModel.getAddresses()

        taskAddViewModel.workersListLiveData().observe(viewLifecycleOwner) { workersList ->
            val workerAdapter = ArrayAdapter(
                requireContext(),
                R.layout.simple_spinner_item,
                workersList.map { "${it.name} ${it.contacts}" }
            )
            Log.d("SCHEDULE", "workersList size: ${workersList.size}")
            workerAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
            binding.workerSpinner.setAdapter(workerAdapter)

            binding.workerSpinner.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    binding.workerSpinner.showDropDown()
                }
            }

            binding.workerSpinner.setOnClickListener {
                binding.workerSpinner.showDropDown()
            }
        }

        taskAddViewModel.addressesListLiveData().observe(viewLifecycleOwner) { addressesList ->
            val addressAdapter = ArrayAdapter(
                requireContext(),
                R.layout.simple_spinner_item,
                addressesList.map { "${it.city} ${it.street}" }
            )
            Log.d("SCHEDULE", "addressesList size: ${addressesList.size}")
            addressAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
            binding.addressSpinner.setAdapter(addressAdapter)

            // Отображение списка сразу при получении фокуса
            binding.addressSpinner.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    binding.addressSpinner.showDropDown()
                }
            }

            // Также добавьте событие нажатия, чтобы выпадающий список отображался при каждом клике
            binding.addressSpinner.setOnClickListener {
                binding.addressSpinner.showDropDown()
            }
        }

        binding.saveButton.setOnClickListener {
            val address = binding.addressSpinner.text.toString()
            val worker = binding.workerSpinner.text.toString()
            val description = binding.descriptionEditText.text.toString()
            val date = taskDayViewModel.dateLiveData().value
            Log.d("SCHEDULE", "date = $date")


            if (address.isNotBlank() && worker.isNotBlank()) {
                val selectedAddress = taskAddViewModel.addressesListLiveData().value?.find { "${it.city} ${it.street}" == address }
                val selectedWorker = taskAddViewModel.workersListLiveData().value?.find { "${it.name} ${it.contacts}" ==  worker}
                Log.d("SCHEDULE", "selectedAddress = ${selectedAddress?.city}, selectedWorker = ${selectedWorker?.name}")
                if (selectedAddress != null && selectedWorker != null) {
                    date?.let {
                        val task = Task(
                            address = selectedAddress,
                            worker = selectedWorker,
                            description = description,
                            date = date
                        )
                        Log.d("SCHEDULE", "address = ${task.address}, worker = ${task.worker}")
                        hideKeyBoard()
                        taskAddViewModel.addTask(task)
                        taskAddViewModel.comeback()
                    }
                }
                else {
                    Toast.makeText(activity, "Выберите существующие параметры", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(activity, "Заполните все обязательные поля", Toast.LENGTH_SHORT).show()
            }

        }
    }

}