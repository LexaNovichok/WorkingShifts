package com.github.lexanovichok.workingshifts.schedule.tasks.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.lexanovichok.workingshifts.R
import com.github.lexanovichok.workingshifts.core.AbstractFragment
import com.github.lexanovichok.workingshifts.core.ProvideViewModel
import com.github.lexanovichok.workingshifts.databinding.FragmentTasksBinding
import com.github.lexanovichok.workingshifts.schedule.tasks.adapter.TasksPagerAdapter
import com.github.lexanovichok.workingshifts.schedule.tasks.viewModel.TasksViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class TasksFragment : AbstractFragment<FragmentTasksBinding>() {

    private lateinit var tasksViewModel : TasksViewModel
    override fun bind(inflater: LayoutInflater, container: ViewGroup?): FragmentTasksBinding =
        FragmentTasksBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tasksViewModel = (activity as ProvideViewModel).viewModel(TasksViewModel::class.java)

        initViewPager()



    }

    private fun initViewPager() = with(binding) {
        val adapter = TasksPagerAdapter(this@TasksFragment)
        viewPager.adapter = adapter
    }
}