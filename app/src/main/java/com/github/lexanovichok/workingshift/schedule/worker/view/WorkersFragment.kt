package com.github.lexanovichok.workingshift.schedule.worker.view

import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.github.lexanovichok.workingshift.R
import com.github.lexanovichok.workingshift.core.AbstractFragment
import com.github.lexanovichok.workingshift.core.ProvideViewModel
import com.github.lexanovichok.workingshift.databinding.FragmentWorkersBinding
import com.github.lexanovichok.workingshift.schedule.userData.Worker
import com.github.lexanovichok.workingshift.schedule.worker.core.WorkersRcViewAdapter
import com.github.lexanovichok.workingshift.schedule.worker.viewModel.WorkersViewModel

class WorkersFragment : AbstractFragment<FragmentWorkersBinding>() {

    private lateinit var workersViewModel : WorkersViewModel
    private lateinit var rcViewAdapter : WorkersRcViewAdapter

    init {
        Log.d("LC", "WorkersFragment: init")
    }

    override fun bind(inflater: LayoutInflater, container: ViewGroup?): FragmentWorkersBinding =
        FragmentWorkersBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("LC", "WorkersFragment: onViewCreated")

        workersViewModel = (activity as ProvideViewModel).viewModel(WorkersViewModel::class.java)
        Log.d("SCHEDULE", "WorkersFragment onViewCreated")

        initRcView()


        binding.addWorkerButton.setOnClickListener {
            workersViewModel.addWorkerFragment()
        }

        workersViewModel.workersListLiveData().observe(viewLifecycleOwner) { list ->
            Log.d("SCHEDULE", "WorkersFragment some changes in workersListLiveData")
            rcViewAdapter.update(ArrayList(list))
        }

    }

    private fun initRcView() = with(binding) {
        rcViewAdapter = WorkersRcViewAdapter(object : WorkersRcViewAdapter.OnWorkerClickListener {
            override fun onClick(worker: Worker) {
                workersViewModel.updateCurrentWorkerFromRcView(worker)
                workersViewModel.workerInfoFragment()
            }

        })
        rcView.adapter = rcViewAdapter

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(rcView)
    }



    private val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            val fromPosition = viewHolder.adapterPosition
            val toPosition = target.adapterPosition

            // Обновляем порядок элементов в адаптере
            rcViewAdapter.moveItem(fromPosition, toPosition)

            // Сохраняем новый порядок в Firebase
            workersViewModel.updateOrderInFirebase(rcViewAdapter.list)

            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.adapterPosition
            val worker = rcViewAdapter.list[position] // Получаем элемент для удаления

            // Удаляем элемент из адаптера и уведомляем его об изменении данных
            workersViewModel.deleteWorker(worker.id )
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