package com.github.lexanovichok.workingshifts.schedule.tasks.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.lexanovichok.workingshifts.core.AbstractFragment
import com.github.lexanovichok.workingshifts.core.ProvideViewModel
import com.github.lexanovichok.workingshifts.schedule.tasks.TasksRcViewAdapter
import com.github.lexanovichok.workingshifts.databinding.FragmentTaskDayBinding
import com.github.lexanovichok.workingshifts.schedule.tasks.viewModel.TaskDayViewModel

class TaskDayFragment : AbstractFragment<FragmentTaskDayBinding>() {

    private lateinit var rcViewAdapter: TasksRcViewAdapter
    private lateinit var taskViewModel: TaskDayViewModel
    override fun bind(inflater: LayoutInflater, container: ViewGroup?): FragmentTaskDayBinding =
        FragmentTaskDayBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        taskViewModel = (activity as ProvideViewModel).viewModel(TaskDayViewModel::class.java)
        initRcView()

        taskViewModel.tasks.observe(viewLifecycleOwner) { tasks ->
            rcViewAdapter.update(ArrayList(tasks))
        }

    }

    private fun initRcView() = with(binding) {
        rcViewAdapter = TasksRcViewAdapter()
        rcView.adapter = rcViewAdapter
    }
    companion object {
        private const val ARG_DAY_OFFSET = "day_offset"

        fun newInstance(dayOffset: Int): TaskDayFragment {
            val fragment = TaskDayFragment()
            val args = Bundle()
            args.putInt(ARG_DAY_OFFSET, dayOffset)
            fragment.arguments = args
            return fragment
        }
    }
}