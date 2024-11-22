package com.github.lexanovichok.workingshift.schedule.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.GravityCompat
import com.github.lexanovichok.workingshift.R
import com.github.lexanovichok.workingshift.core.AbstractFragment
import com.github.lexanovichok.workingshift.core.ProvideViewModel
import com.github.lexanovichok.workingshift.databinding.FragmentMainBinding

class MainFragment : AbstractFragment<FragmentMainBinding>() {

    private lateinit var mainFragmentViewModel: MainFragmentViewModel

    init {
        Log.d("LC", "MainFragment init")
    }
    override fun bind(inflater: LayoutInflater, container: ViewGroup?): FragmentMainBinding =
        FragmentMainBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("LC", "MainFragment: onViewCreated")

        mainFragmentViewModel =
            (activity as ProvideViewModel).viewModel(MainFragmentViewModel::class.java)




        val selectedItemId = savedInstanceState?.getInt("selected_item_id") ?: R.id.nav_tasks
        binding.bottomNavigationView.selectedItemId = selectedItemId
        Log.d("Bundle", "MainFragment restoreState: $selectedItemId")

        mainFragmentViewModel.liveData().observe(viewLifecycleOwner) { screen ->
            screen.show(childFragmentManager, binding.content.id)
            //Log.d("NAVIGATION", "MainFragment livedata screen: $screen")
        }

        if (savedInstanceState == null) {
            mainFragmentViewModel.init(true)
        }

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
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

        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_history -> {
                    mainFragmentViewModel.tasksHistoryFragment()
                    true
                }
                else -> false
            }.also {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            }
        }

        val logoutClickable = binding.navigationView.findViewById<LinearLayout>(R.id.logoutClickable)

        logoutClickable.setOnClickListener {
            mainFragmentViewModel.logout()
            Toast.makeText(activity, "Вы вышли из аккаунта", Toast.LENGTH_SHORT).show()

            val intent = requireActivity().intent
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            requireActivity().finish() // Закрываем текущую активность
            startActivity(intent) // Перезапускаем

            binding.drawerLayout.closeDrawer(GravityCompat.START)

        }


        // --- Работа с темой ---
        val sharedPreferences = requireContext().getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)
        val switchTheme = binding.navigationView.findViewById<SwitchCompat>(R.id.switch_theme)

        // Устанавливаем начальное состояние переключателя
        val isDarkMode = sharedPreferences.getBoolean("isDarkMode", false)
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )
        switchTheme.isChecked = isDarkMode


        switchTheme.setOnCheckedChangeListener { _, isChecked ->
            val editor = sharedPreferences.edit()
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                editor.putBoolean("isDarkMode", true)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                editor.putBoolean("isDarkMode", false)
            }
            editor.apply()
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("selected_item_id", binding.bottomNavigationView.selectedItemId)
        Log.d("Bundle", "MainFragment onSaveInstanceState: ${binding.bottomNavigationView.selectedItemId}")
    }



}