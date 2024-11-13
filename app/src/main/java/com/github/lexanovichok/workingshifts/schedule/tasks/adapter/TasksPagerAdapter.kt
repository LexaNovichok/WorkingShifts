package com.github.lexanovichok.workingshifts.schedule.tasks.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.github.lexanovichok.workingshifts.schedule.tasks.view.TaskDayFragment

class TasksPagerAdapter(fragment : Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 30
    }

    override fun createFragment(position: Int): Fragment {
        return TaskDayFragment.newInstance(position)
    }
}