package com.github.lexanovichok.workingshift.schedule.worker.core

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.github.lexanovichok.workingshift.R
import com.github.lexanovichok.workingshift.databinding.WorkerRcviewItemBinding
import com.github.lexanovichok.workingshift.schedule.userData.Worker
import java.util.Collections

class WorkersRcViewAdapter(private val listener : OnWorkerClickListener) : RecyclerView.Adapter<WorkersRcViewAdapter.ViewHolder>() {
    internal val list : ArrayList<Worker> = arrayListOf()

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        private val binding = WorkerRcviewItemBinding.bind(view)

        fun bind(worker : Worker, listener: OnWorkerClickListener) = with(binding) {
            workerNameText.text = worker.name
            contactInfoText.text = worker.contacts

            itemView.setOnClickListener {
                listener.onClick(worker)
            }
        }
    }

    fun update(newList : ArrayList<Worker>) {
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
        val view = LayoutInflater.from(parent.context).inflate(R.layout.worker_rcview_item, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position], listener)
    }

    class DiffUtilCallBack(
        private val old : List<Worker>,
        private val new : List<Worker>
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

            return oldItem.name == newItem.name &&
                    oldItem.contacts == newItem.contacts &&
                    oldItem.description == newItem.description
        }

    }

    interface OnWorkerClickListener  {
        fun onClick(worker : Worker)
    }
}