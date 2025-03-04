package com.github.lexanovichok.workingshift.schedule.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager


interface ScreenF {

    fun show(childFragmentManager: FragmentManager, containerId : Int)

    abstract class Replace(private val fragmentClass : Class<out Fragment>) : ScreenF {
        override fun show(childFragmentManager: FragmentManager, containerId: Int) {
            childFragmentManager
                .beginTransaction()
                .replace(containerId, fragmentClass.getDeclaredConstructor().newInstance())
                .addToBackStack(fragmentClass.name)
                .commit()
        }
    }

    abstract class Add(private val fragmentClass : Class<out Fragment>) : ScreenF {
        override fun show(childFragmentManager: FragmentManager, containerId: Int) {
            childFragmentManager
                .beginTransaction()
                .add(containerId, fragmentClass.getDeclaredConstructor().newInstance())
                .addToBackStack(fragmentClass.name)
                .commit()
        }
    }

    object Pop : ScreenF {
        override fun show(childFragmentManager: FragmentManager, containerId: Int) {
            childFragmentManager.popBackStack()
        }
    }
}