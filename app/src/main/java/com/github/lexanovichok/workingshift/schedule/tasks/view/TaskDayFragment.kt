package com.github.lexanovichok.workingshift.schedule.tasks.view

import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.github.lexanovichok.workingshift.R
import com.github.lexanovichok.workingshift.core.AbstractFragment
import com.github.lexanovichok.workingshift.core.ProvideViewModel
import com.github.lexanovichok.workingshift.schedule.tasks.adapter.TasksRcViewAdapter
import com.github.lexanovichok.workingshift.databinding.FragmentTaskDayBinding
import com.github.lexanovichok.workingshift.schedule.tasks.viewModel.TaskDayViewModel
import com.github.lexanovichok.workingshift.schedule.userData.Task

class TaskDayFragment : AbstractFragment<FragmentTaskDayBinding>() {

    private lateinit var rcViewAdapter: TasksRcViewAdapter
    private lateinit var viewModel: TaskDayViewModel
    override fun bind(inflater: LayoutInflater, container: ViewGroup?): FragmentTaskDayBinding =
        FragmentTaskDayBinding.inflate(inflater, container, false)

    init {
        Log.d("LC", "MainTaskDayFragmentActivity init")
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as ProvideViewModel).viewModel(TaskDayViewModel::class.java)
        initRcView()

        val date = arguments?.getString(ARG_DATE)
        binding.dateTextView.text = date

        viewModel.tasksListLiveData().observe(viewLifecycleOwner) { list ->
            val filteredList = list.filter { it.date == date } // фильтрация задач по текущей дате
            rcViewAdapter.update(ArrayList(filteredList))
        }

        binding.addTaskButton.setOnClickListener {
            viewModel.setDateToLiveData(date)
            viewModel.addTaskFragment()
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Проверяем, является ли родительский фрагмент TasksFragment, и вызываем метод для возврата к текущей дате
                (parentFragment as? TasksFragment)?.scrollToCurrentDate()
            }
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(ARG_DATE_FOR_TASK_ADD, arguments?.getString(ARG_DATE))
    }

    private fun initRcView() = with(binding) {
        rcViewAdapter = TasksRcViewAdapter(object : TasksRcViewAdapter.OnTaskClickListener {
            override fun onClick(task: Task) {
                viewModel.updateCurrentTaskFromRcView(task)
                viewModel.infoTaskFragment()
            }

        })
        rcView.adapter = rcViewAdapter

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(rcView)
    }

    companion object {
        private const val ARG_DATE = "date"
        private const val ARG_DATE_FOR_TASK_ADD = "date_for_task_add"

        fun newInstance(date: String): TaskDayFragment {
            val fragment = TaskDayFragment()
            val args = Bundle()
            args.putString(ARG_DATE, date)
            fragment.arguments = args
            return fragment
        }
    }

    //Item touch helper (swipes on rcView items)
    private val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return false // Не нужно обрабатывать перемещение
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.adapterPosition
            val worker = rcViewAdapter.list[position] // Получаем элемент для удаления

            // Удаляем элемент из адаптера и уведомляем его об изменении данных
            viewModel.deleteTask(worker.id )
        }

        override fun onChildDraw(
            c: Canvas,
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            dX: Float,
            dY: Float,
            actionState: Int,
            isCurrentlyActive: Boolean
        ) {
            // Отрисовываем кнопку удаления при свайпе
            val deleteIcon = ContextCompat.getDrawable(recyclerView.context, R.drawable.trash)!!
            val itemView = viewHolder.itemView
            val iconMargin = (itemView.height - deleteIcon.intrinsicHeight) / 2

            if (dX < 0) { // Если свайп влево
                val iconTop = itemView.top + (itemView.height - deleteIcon.intrinsicHeight) / 2
                val iconBottom = iconTop + deleteIcon.intrinsicHeight
                val iconLeft = itemView.right - iconMargin - deleteIcon.intrinsicWidth
                val iconRight = itemView.right - iconMargin

                deleteIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                deleteIcon.draw(c)
            }

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }
    }
}