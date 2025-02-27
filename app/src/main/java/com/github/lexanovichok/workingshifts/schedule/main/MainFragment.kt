package com.github.lexanovichok.workingshifts.schedule.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import com.github.lexanovichok.workingshifts.R
import com.github.lexanovichok.workingshifts.core.AbstractFragment
import com.github.lexanovichok.workingshifts.core.ProvideViewModel
import com.github.lexanovichok.workingshifts.databinding.FragmentMainBinding

class MainFragment : AbstractFragment<FragmentMainBinding>() {

    private lateinit var mainFragmentViewModel : MainFragmentViewModel
    override fun bind(inflater: LayoutInflater, container: ViewGroup?): FragmentMainBinding =
        FragmentMainBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainFragmentViewModel = (activity as ProvideViewModel).viewModel(MainFragmentViewModel::class.java)

        mainFragmentViewModel.liveData().observe(viewLifecycleOwner) { screen ->
            screen.show(childFragmentManager, binding.content.id)
        }

        mainFragmentViewModel.init()

        binding.bottomNavigationView.setOnItemSelectedListener { item->
            when(item.itemId) {
                R.id.nav_workers -> {
                    mainFragmentViewModel.workersFragment()
                    true
                }
                R.id.nav_addresses -> {
                    mainFragmentViewModel.addressesFragment()
                    true
                }
                R.id.nav_tasks -> {
                    mainFragmentViewModel.tasksFragment()
                    true
                }


                else -> false
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            // Проверяем стек фрагментов внутри MainFragment
            if (childFragmentManager.backStackEntryCount > 0) {
                // Если в стеке есть фрагменты, удаляем верхний
                childFragmentManager.popBackStack()
            } else {
                // Если стек пуст, вызываем стандартное поведение (выйти из Activity)
                requireActivity().onBackPressed()
            }
        }
    }


}