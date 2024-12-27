package com.github.lexanovichok.workingshift.schedule.address.view

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
import com.github.lexanovichok.workingshift.databinding.FragmentAddressesBinding
import com.github.lexanovichok.workingshift.schedule.address.core.AddressesRcViewAdapter
import com.github.lexanovichok.workingshift.schedule.address.viewModel.AddressesViewModel
import com.github.lexanovichok.workingshift.schedule.userData.Address
import kotlinx.coroutines.launch

class AddressesFragment : AbstractFragment<FragmentAddressesBinding>() {

    private lateinit var viewModel : AddressesViewModel
    private lateinit var loginViewModel : LoginViewModel
    private lateinit var rcViewAdapter : AddressesRcViewAdapter
    private var isAdmin: Boolean = false

    init {
        Log.d("LC", "AddressesFragment init")
    }
    override fun bind(inflater: LayoutInflater, container: ViewGroup?): FragmentAddressesBinding =
        FragmentAddressesBinding.inflate(inflater, container, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginViewModel = (activity as ProvideViewModel).viewModel(LoginViewModel::class.java)

        lifecycleScope.launch {
            val userState = loginViewModel.checkUserStatus()
            isAdmin = userState.isAdmin
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as ProvideViewModel).viewModel(AddressesViewModel::class.java)

        initRcView()

        if (isAdded && binding != null) {
            Log.d("ROLE", "AddressesFragment isAdmin: $isAdmin")
            binding?.addAddressButton?.visibility = if (isAdmin) View.VISIBLE else View.GONE
        }

        Log.d("LC", "AddressesFragment onViewCreated")
        binding.addAddressButton.setOnClickListener {
            viewModel.addAddressFragment()
        }

        viewModel.addressesListLiveData().observe(viewLifecycleOwner) { list ->
            rcViewAdapter.update(ArrayList(list))
            Log.d("LiveData", "AddressesFragment addressesListLiveData rcView updated with: $list")
        }
    }

    private fun initRcView() = with(binding) {
        rcViewAdapter = AddressesRcViewAdapter(object : AddressesRcViewAdapter.OnAddressClickListener {
            override fun onClick(address: Address) {
                viewModel.updateAddressFromRcView(address)
                Log.d("LiveData", "Go to item with address: $address")
                viewModel.addressInfoFragment()
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
            viewModel.updateOrderInFirebase(rcViewAdapter.list)

            return true
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