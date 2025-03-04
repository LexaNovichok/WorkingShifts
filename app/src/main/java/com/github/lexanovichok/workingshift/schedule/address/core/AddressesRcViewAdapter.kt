package com.github.lexanovichok.workingshift.schedule.address.core

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.github.lexanovichok.workingshift.R
import com.github.lexanovichok.workingshift.databinding.AddressRcviewItemBinding
import com.github.lexanovichok.workingshift.schedule.userData.Address
import java.util.Collections

class AddressesRcViewAdapter(private val listener : OnAddressClickListener) : RecyclerView.Adapter<AddressesRcViewAdapter.ViewHolder>() {
    internal val list : ArrayList<Address> = arrayListOf()

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        private val binding = AddressRcviewItemBinding.bind(view)

        fun bind(address : Address, listener: OnAddressClickListener) = with(binding) {
            cityTextView.text = address.city
            streetTextView.text = address.street

            itemView.setOnClickListener {
                listener.onClick(address)
            }
        }
    }

    fun update(newList : ArrayList<Address>) {
        val diffUtil = DiffUtilCallBack(list, newList)
        val diff = DiffUtil.calculateDiff(diffUtil)

        list.clear()
        list.addAll(newList)
        diff.dispatchUpdatesTo(this)
    }

    fun moveItem(fromPosition: Int, toPosition: Int) {
        Collections.swap(list, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.address_rcview_item, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position], listener)
    }

    class DiffUtilCallBack(
        private val old : List<Address>,
        private val new : List<Address>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = old.size

        override fun getNewListSize(): Int = new.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = old[oldItemPosition]
            val newItem = new[newItemPosition]

            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = old[oldItemPosition]
            val newItem = new[newItemPosition]

            return oldItem.city == newItem.city &&
                    oldItem.street == newItem.street &&
                    oldItem.description == newItem.description
        }

    }

    interface OnAddressClickListener  {
        fun onClick(address : Address)
    }
}