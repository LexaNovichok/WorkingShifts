package com.github.lexanovichok.workingshifts.schedule.tasks.adapter

import android.content.Context
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import com.github.lexanovichok.workingshifts.R
import com.github.lexanovichok.workingshifts.schedule.userData.Worker

class WorkerDropdownAdapter(
    private val context: Context,
    private val workers: List<Worker>
) : BaseAdapter(), Filterable {

    private var filteredWorkers: List<Worker> = workers
    override fun getCount(): Int = workers.size

    override fun getItem(position: Int): Worker = workers[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.custom_dropdown_worker_item, parent, false)

        val worker = getItem(position)

        //view.findViewById<ImageView>(R.id.workerIcon).setImageResource(worker.)
        view.findViewById<TextView>(R.id.workerName).text = worker.name
        view.findViewById<TextView>(R.id.workerContacts).text = worker.contacts

        return view
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredList = if (constraint.isNullOrEmpty()) {
                    workers
                } else {
                    workers.filter {
                        it.name.contains(constraint, ignoreCase = true) ||
                                it.contacts.contains(constraint, ignoreCase = true)
                    }
                }

                return FilterResults().apply {
                    values = filteredList
                }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredWorkers = results?.values as List<Worker>
                notifyDataSetChanged()
            }
        }
    }
}