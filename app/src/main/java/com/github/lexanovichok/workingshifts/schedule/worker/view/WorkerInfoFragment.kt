package com.github.lexanovichok.workingshifts.schedule.worker.view

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.github.lexanovichok.workingshifts.core.AbstractFragment
import com.github.lexanovichok.workingshifts.core.ProvideViewModel
import com.github.lexanovichok.workingshifts.databinding.FragmentWorkerInfoBinding
import com.github.lexanovichok.workingshifts.schedule.userData.Worker
import com.github.lexanovichok.workingshifts.schedule.worker.viewModel.WorkerInfoViewModel
import kotlin.random.Random

class WorkerInfoFragment : AbstractFragment<FragmentWorkerInfoBinding>() {

    private lateinit var workerInfoViewModel: WorkerInfoViewModel

    init {
        Log.d("LC", "WorkerInfoFragment: init")
    }
    override fun bind(inflater: LayoutInflater, container: ViewGroup?): FragmentWorkerInfoBinding =
        FragmentWorkerInfoBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("LC", "WorkerInfoFragment: onViewCreated")

        workerInfoViewModel = (activity as ProvideViewModel).viewModel(WorkerInfoViewModel::class.java)

        workerInfoViewModel.workerInfoLiveData().observe(viewLifecycleOwner) { worker ->

            if (worker != null) {
                with(binding) {
                    nameEditText.setText(worker.name)
                    contactsEditText.setText(worker.contacts)
                    descriptionEditText.setText(worker.description)
                }
            } else {
                with(binding) {
                    nameEditText.setText("")
                    contactsEditText.setText("")
                    descriptionEditText.setText("")
                }
            }

            Log.d("LiveData", "WorkerInfoFragment workerInfoLiveData updated with: name: ${worker.name}, contacts: ${worker.contacts}, description: ${worker.description}")
        }

        binding.saveButton.setOnClickListener {
            val id = workerInfoViewModel.workerInfoLiveData().value?.id ?: Random(128).toString()
            val name = binding.nameEditText.text.toString()
            val contacts = binding.contactsEditText.text.toString()
            val description = binding.descriptionEditText.text.toString()

            val worker = Worker(id, name, contacts, description)
            hideKeyBoard()
            workerInfoViewModel.updateWorker(worker)
        }

        binding.deleteButton.setOnClickListener {
            workerInfoViewModel.workerInfoLiveData().value?.let {
                showDeleteConfirmationDialog()
            }
        }
    }

    private fun showDeleteConfirmationDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("Вы уверены, что хотите удалить этого работника?")
            .setPositiveButton("Удалить") { dialog, id ->
                // Получаем информацию о работнике и выполняем удаление
                workerInfoViewModel.workerInfoLiveData().value?.let {
                    hideKeyBoard()
                    workerInfoViewModel.deleteWorker(it.id)
                    //Toast.makeText(requireContext(), "Работник удалён", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Отмена") { dialog, id ->
                dialog.dismiss() // Закрытие диалога без действия
            }

        val dialog = builder.create()
        dialog.show()
    }
}