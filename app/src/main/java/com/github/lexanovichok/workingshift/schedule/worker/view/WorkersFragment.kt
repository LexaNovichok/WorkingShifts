package com.github.lexanovichok.workingshift.schedule.worker.view

import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.github.lexanovichok.workingshift.R
import com.github.lexanovichok.workingshift.authentication.login.LoginViewModel
import com.github.lexanovichok.workingshift.core.AbstractFragment
import com.github.lexanovichok.workingshift.core.ProvideViewModel
import com.github.lexanovichok.workingshift.databinding.FragmentWorkersBinding
import com.github.lexanovichok.workingshift.schedule.userData.Worker
import com.github.lexanovichok.workingshift.schedule.worker.core.WorkersRcViewAdapter
import com.github.lexanovichok.workingshift.schedule.worker.viewModel.WorkersViewModel
import kotlinx.coroutines.launch

class WorkersFragment : AbstractFragment<FragmentWorkersBinding>() {

    private lateinit var workersViewModel : WorkersViewModel
    private lateinit var loginViewModel : LoginViewModel
    private lateinit var rcViewAdapter : WorkersRcViewAdapter
    private var isAdmin: Boolean = false

    init {
        Log.d("LC", "WorkersFragment: init")
    }

    override fun bind(inflater: LayoutInflater, container: ViewGroup?): FragmentWorkersBinding =
        FragmentWorkersBinding.inflate(inflater, container, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        workersViewModel = (activity as ProvideViewModel).viewModel(WorkersViewModel::class.java)
        loginViewModel = (activity as ProvideViewModel).viewModel(LoginViewModel::class.java)

        lifecycleScope.launch {
            try {
                val userState = loginViewModel.checkUserStatus()
                isAdmin = userState.isAdmin
                Log.d("ROLES", "isAdmin: $isAdmin")

                // Обновляем UI, когда статус получен
                activity?.runOnUiThread {
                    binding?.addWorkerButton?.visibility = if (isAdmin) View.VISIBLE else View.GONE
                    Log.d(
                        "ROLE",
                        "button onCreate visibility: ${binding?.addWorkerButton?.isVisible}"
                    )

                }
            } catch (e: Exception) {
                Log.e("TaskDayFragment", "Error fetching user status", e)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRcView()

        if (isAdded && binding != null) {
            binding?.addWorkerButton?.visibility = if (isAdmin) View.VISIBLE else View.GONE
        }

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