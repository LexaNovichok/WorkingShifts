package com.github.lexanovichok.workingshifts.schedule.worker.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.lexanovichok.workingshifts.core.AbstractFragment
import com.github.lexanovichok.workingshifts.core.ProvideViewModel
import com.github.lexanovichok.workingshifts.databinding.FragmentWorkersBinding
import com.github.lexanovichok.workingshifts.schedule.worker.viewModel.WorkersViewModel

class WorkersFragment : AbstractFragment<FragmentWorkersBinding>() {

    private lateinit var workersViewModel : WorkersViewModel
    override fun bind(inflater: LayoutInflater, container: ViewGroup?): FragmentWorkersBinding =
        FragmentWorkersBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        workersViewModel = (activity as ProvideViewModel).viewModel(WorkersViewModel::class.java)

        binding.addWorkerButton.setOnClickListener {
            workersViewModel.addWorkerFragment()
        }
    }
}