package com.github.lexanovichok.workingshift.schedule.tasks.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.lexanovichok.workingshift.authentication.login.LoginViewModel
import com.github.lexanovichok.workingshift.core.AbstractFragment
import com.github.lexanovichok.workingshift.core.ProvideViewModel
import com.github.lexanovichok.workingshift.databinding.FragmentTaskHistoryBinding
import com.github.lexanovichok.workingshift.schedule.tasks.adapter.TasksHistoryRcViewAdapter
import com.github.lexanovichok.workingshift.schedule.tasks.viewModel.TaskDayViewModel
import com.github.lexanovichok.workingshift.schedule.tasks.viewModel.TaskHistoryViewModel
import com.github.lexanovichok.workingshift.schedule.userData.Task

class TaskHistoryFragment : AbstractFragment<FragmentTaskHistoryBinding>() {

    private lateinit var rcViewAdapter : TasksHistoryRcViewAdapter
    private lateinit var taskDayViewModel: TaskDayViewModel
    private lateinit var taskHistoryViewModel : TaskHistoryViewModel
    private lateinit var loginViewModel : LoginViewModel
    override fun bind(inflater: LayoutInflater, container: ViewGroup?): FragmentTaskHistoryBinding =
        FragmentTaskHistoryBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        taskDayViewModel = (activity as ProvideViewModel).viewModel(TaskDayViewModel::class.java)
        taskHistoryViewModel = (activity as ProvideViewModel).viewModel(TaskHistoryViewModel::class.java)
        loginViewModel = (activity as ProvideViewModel).viewModel(LoginViewModel::class.java)

        initRcView()

        taskHistoryViewModel.tasksHistoryLiveData().observe(viewLifecycleOwner) { list ->
            Log.d("SCHEDULE", "tasksHistoryLiveData updated: $list")
            rcViewAdapter.update(list)
        }
    }

    private fun initRcView() = with(binding) {
        rcViewAdapter =
            TasksHistoryRcViewAdapter(object : TasksHistoryRcViewAdapter.OnTaskClickListener {
                override fun onClick(task: Task) {
                    if (loginViewModel.adminLiveData().value == true) {
                        taskDayViewModel.updateCurrentTaskFromRcView(task)
                        taskDayViewModel.infoTaskFragment()
                    }
                }

            })
        rcView.adapter = rcViewAdapter
    }

}