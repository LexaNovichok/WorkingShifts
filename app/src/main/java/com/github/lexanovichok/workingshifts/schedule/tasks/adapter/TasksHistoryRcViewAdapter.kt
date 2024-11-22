package com.github.lexanovichok.workingshifts.schedule.tasks.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.github.lexanovichok.workingshifts.R
import com.github.lexanovichok.workingshifts.databinding.HeaderRcviewItemBinding
import com.github.lexanovichok.workingshifts.databinding.TaskRcviewItemBinding
import com.github.lexanovichok.workingshifts.schedule.userData.ListItem
import com.github.lexanovichok.workingshifts.schedule.userData.Task

class TasksHistoryRcViewAdapter(
    private val listener: OnTaskClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items: ArrayList<ListItem> = arrayListOf()

    fun update(newList: List<Task>) {

        // Группируем задачи по дате
        val groupedTasks = newList.groupBy { it.date }

        // Создаём список ListItem
        val newItems = groupedTasks.flatMap { (date, tasks) ->
            listOf(
                ListItem.HeaderItem(date)) + tasks.map { ListItem.TaskItem(it) }
        }

        val diffUtil = DiffUtilCallBack(items, newItems)
        val diff = DiffUtil.calculateDiff(diffUtil)

        items.clear()
        items.addAll(newItems)
        diff.dispatchUpdatesTo(this)
    }

    inner class HeaderItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = HeaderRcviewItemBinding.bind(itemView)
        fun bind(item: ListItem.HeaderItem) = with(binding) {
            dateText.text = item.text
        }
    }

    inner class TaskItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = TaskRcviewItemBinding.bind(itemView)

        fun bind(item: ListItem.TaskItem) = with(binding) {
            val addressText = if (item.task.address.street.isNotBlank()) {
                "${item.task.address.city}, ${item.task.address.street}"
            } else {
                item.task.address.city
            }

            tvWorkerName.text = item.task.worker.name
            tvLocation.text = addressText

            itemView.setOnClickListener {
                listener.onClick(item.task)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is ListItem.HeaderItem -> 0
            is ListItem.TaskItem -> 1
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            0 -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.header_rcview_item, parent, false)
                HeaderItemViewHolder(view)
            }

            1 -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.task_rcview_item, parent, false)
                TaskItemViewHolder(view)
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]) {
            is ListItem.HeaderItem -> (holder as HeaderItemViewHolder).bind(item)
            is ListItem.TaskItem -> (holder as TaskItemViewHolder).bind(item)
        }
    }


    interface OnTaskClickListener {
        fun onClick(task: Task)
    }

    class DiffUtilCallBack(
        private val oldList: List<ListItem>,
        private val newList: List<ListItem>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]

            return when {
                oldItem is ListItem.HeaderItem && newItem is ListItem.HeaderItem -> {
                    oldItem.text == newItem.text // Даты могут быть одинаковыми
                }
                oldItem is ListItem.TaskItem && newItem is ListItem.TaskItem -> {
                    oldItem.task.id == newItem.task.id // Сравниваем задачи по ID
                }
                else -> false
            }
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}