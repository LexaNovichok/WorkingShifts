package com.github.lexanovichok.workingshift.schedule.tasks.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.lexanovichok.workingshift.core.AbstractFragment
import com.github.lexanovichok.workingshift.databinding.FragmentTasksBinding
import com.github.lexanovichok.workingshift.schedule.tasks.adapter.TasksPagerAdapter


class TasksFragment : AbstractFragment<FragmentTasksBinding>() {

    //private lateinit var tasksViewModel : TasksViewModel

    init {
        Log.d("LC", "TasksFragment init")
    }
    override fun bind(inflater: LayoutInflater, container: ViewGroup?): FragmentTasksBinding =
        FragmentTasksBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("LC", "MainActivity onViewCreated")

        //tasksViewModel = (activity as ProvideViewModel).viewModel(TasksViewModel::class.java)

        initViewPager()
    }

    private fun initViewPager() = with(binding) {
        val adapter = TasksPagerAdapter(this@TasksFragment)
        viewPager.adapter = adapter
        viewPager.setCurrentItem(3, false)
    }

    fun scrollToCurrentDate() {
        binding.viewPager.setCurrentItem(3, true)
    }
}