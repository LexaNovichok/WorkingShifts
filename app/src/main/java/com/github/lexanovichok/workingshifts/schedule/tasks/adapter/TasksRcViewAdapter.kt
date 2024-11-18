package com.github.lexanovichok.workingshifts.schedule.tasks.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.github.lexanovichok.workingshifts.R
import com.github.lexanovichok.workingshifts.schedule.userData.Task
import com.github.lexanovichok.workingshifts.databinding.TaskRcviewItemBinding

class TasksRcViewAdapter(private val listener : OnTaskClickListener) : RecyclerView.Adapter<TasksRcViewAdapter.ViewHolder>() {

    val list : ArrayList<Task> = arrayListOf()
    class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        private val binding = TaskRcviewItemBinding.bind(view)

        fun bind(task: Task, listener : OnTaskClickListener) = with(binding) {
            val addressText = "${task.address.city}, ${task.address.street}"

            tvWorkerName.text = task.worker.name
            tvLocation.text = addressText

            itemView.setOnClickListener {
                listener.onClick(task)
            }
        }
    }

    fun update(newList : ArrayList<Task>) {
        val diffUtil = DiffUtilCallBack(list, newList)
        val diff = DiffUtil.calculateDiff(diffUtil)

        list.clear()
        list.addAll(newList)
        Log.d("SCHEDULE", "TasksRcViewAdapter update")
        diff.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_rcview_item, parent, false)
        Log.d("SCHEDULE", "TasksRcViewAdapter onCreateViewHolder")
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position], listener)
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

            return oldItem.worker.id == newItem.worker.id && oldItem.address.id == newItem.address.id

    }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = old[oldItemPosition]
            val newItem = new[newItemPosition]

            return oldItem.worker.name == newItem.worker.name &&
                    oldItem.worker.contacts == newItem.worker.contacts &&
                    oldItem.address.city == newItem.address.city &&
                    oldItem.address.street == newItem.address.street &&
                    oldItem.address.houseNum == newItem.address.houseNum &&
                    oldItem.date == newItem.date
        }

    }

    interface OnTaskClickListener {
        fun onClick(task : Task)
    }
}