package com.github.lexanovichok.workingshift.main

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

interface ScreenA {

    fun show(supportFragmentManager: FragmentManager, containerId : Int)

    abstract class Replace(private val fragmentClass : Class<out Fragment>) : ScreenA {

        override fun show(supportFragmentManager: FragmentManager, containerId: Int) {
            val tag = fragmentClass.name // Уникальный тег для фрагмента
            val existingFragment = supportFragmentManager.findFragmentByTag(tag)

            if (existingFragment == null) {
                // Если фрагмент не существует, создаем его и добавляем
                supportFragmentManager
                    .beginTransaction()
                    .replace(containerId, fragmentClass.getDeclaredConstructor().newInstance(), tag)
                    .addToBackStack(tag)
                    .commit()
            } else {
                // Если фрагмент существует, можно дополнительно обработать (например, вывести лог)
                Log.d("LC", "Fragment with tag $tag already exists, skipping creation.")
            }
        }
    }


    abstract class Add(private val fragmentClass: Class<out Fragment>) : ScreenA {

        override fun show(supportFragmentManager: FragmentManager, containerId: Int) {
            supportFragmentManager
                .beginTransaction()
                .add(containerId, fragmentClass.getDeclaredConstructor().newInstance())
                .addToBackStack(fragmentClass.name)
                .commit()
        }
    }

    abstract class ReplaceMain(private val fragmentClass: Class<out Fragment>) : ScreenA {

        override fun show(supportFragmentManager: FragmentManager, containerId: Int) {
            supportFragmentManager
                .beginTransaction()
                .replace(containerId, fragmentClass.getDeclaredConstructor().newInstance())
                .commit()
        }
    }

    object Pop : ScreenA {
        override fun show(supportFragmentManager: FragmentManager, containerId: Int) {
            supportFragmentManager.popBackStack()
        }
    }
}