package com.github.lexanovichok.workingshift.schedule.tasks.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.github.lexanovichok.workingshift.schedule.tasks.view.TaskDayFragment
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class TasksPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    private val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val baseDate: Calendar = Calendar.getInstance() // Текущая дата

    override fun getItemCount(): Int = 7

    override fun createFragment(position: Int): Fragment {
        val date = getDateForPosition(position)
        return TaskDayFragment.newInstance(date)
    }

    private fun getDateForPosition(position: Int): String {
        // Клонируем базовую дату (текущую), чтобы не менять оригинал
        val calendar = baseDate.clone() as Calendar
        // Смещение рассчитываем от позиции 3, которая соответствует текущей дате
        val daysOffset = position - 3
        calendar.add(Calendar.DAY_OF_YEAR, daysOffset)
        return dateFormatter.format(calendar.time)
    }

    fun getPositionForDate(date: String): Int {
        val targetDate = Calendar.getInstance().apply {
            time = dateFormatter.parse(date) ?: return 3 // Если не удалось распарсить дату, возвращаем центральную позицию (сегодняшний день)
        }
        // Рассчитываем разницу в днях между целевой и базовой датами
        val daysDifference = ((targetDate.timeInMillis - baseDate.timeInMillis) / (24 * 60 * 60 * 1000)).toInt()
        // Центральная позиция — это позиция 3, где отображается текущая дата
        return 3 + daysDifference
    }
}