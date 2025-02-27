package com.github.lexanovichok.workingshifts.schedule.address.view

import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.github.lexanovichok.workingshifts.R
import com.github.lexanovichok.workingshifts.core.AbstractFragment
import com.github.lexanovichok.workingshifts.core.ProvideViewModel
import com.github.lexanovichok.workingshifts.databinding.FragmentAddressesBinding
import com.github.lexanovichok.workingshifts.schedule.address.core.AddressesRcViewAdapter
import com.github.lexanovichok.workingshifts.schedule.address.viewModel.AddressesViewModel
import com.github.lexanovichok.workingshifts.schedule.userData.Address
import com.github.lexanovichok.workingshifts.schedule.userData.Worker
import com.github.lexanovichok.workingshifts.schedule.worker.core.WorkersRcViewAdapter

class AddressesFragment : AbstractFragment<FragmentAddressesBinding>() {

    private lateinit var viewModel : AddressesViewModel
    private lateinit var rcViewAdapter : AddressesRcViewAdapter

    override fun bind(inflater: LayoutInflater, container: ViewGroup?): FragmentAddressesBinding =
        FragmentAddressesBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as ProvideViewModel).viewModel(AddressesViewModel::class.java)
        initRcView()

        binding.addAddressButton.setOnClickListener {
            viewModel.addAddressFragment()
        }

        viewModel.addressesListLiveData().observe(viewLifecycleOwner) { list ->
            rcViewAdapter.update(ArrayList(list))
        }
    }

    private fun initRcView() = with(binding) {
        rcViewAdapter = AddressesRcViewAdapter(object : AddressesRcViewAdapter.OnAddressClickListener {
            override fun onClick(address: Address) {
                viewModel.updateAddressFromRcView(address)
                viewModel.addressInfoFragment()
            }

        })
        rcView.adapter = rcViewAdapter

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(rcView)
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
            viewModel.deleteAddress(worker.id )
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