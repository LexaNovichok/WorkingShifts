package com.github.lexanovichok.workingshifts.core

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.github.lexanovichok.workingshifts.schedule.userData.Adress
import com.github.lexanovichok.workingshifts.R
import com.github.lexanovichok.workingshifts.schedule.userData.Task
import com.github.lexanovichok.workingshifts.schedule.userData.Worker
import com.github.lexanovichok.workingshifts.databinding.TaskRcviewItemBinding

class TasksRcViewAdapter : RecyclerView.Adapter<TasksRcViewAdapter.ViewHolder>() {

    private val list : ArrayList<Task> = arrayListOf()
    class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        private val binding = TaskRcviewItemBinding.bind(view)

        fun bind(worker : Worker, adress : Adress) = with(binding) {
            val adressText = "${adress.city}, ${adress.street}"

            tvWorkerName.text = worker.name
            tvLocation.text = adressText
        }
    }

    fun update(newList : ArrayList<Task>) {
        val diffUtil = DiffUtilCallBack(list, newList)
        val diff = DiffUtil.calculateDiff(diffUtil)

        list.clear()
        list.addAll(newList)
        diff.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_rcview_item, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position].worker, list[position].adress)
    }

    class DiffUtilCallBack(
        private val old : List<Task>,
        private val new : List<Task>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = old.size

        override fun getNewListSize(): Int = new.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = old[oldItemPosition]
            val newItem = new[newItemPosition]

            return oldItem.worker.id == newItem.worker.id && oldItem.adress.id == newItem.adress.id
    }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = old[oldItemPosition]
            val newItem = new[newItemPosition]

            return oldItem.worker.name == newItem.worker.name &&
                    oldItem.worker.contacts == newItem.worker.contacts &&
                    oldItem.adress.city == newItem.adress.city &&
                    oldItem.adress.street == newItem.adress.street &&
                    oldItem.adress.houseNum == newItem.adress.houseNum &&
                    oldItem.date == newItem.date
        }

    }
}