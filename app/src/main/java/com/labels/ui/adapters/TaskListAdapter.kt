package com.labels.ui.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.labels.R
import com.labels.model.Task
import com.labels.ui.activity.TaskDetailsActivity
import kotlinx.android.synthetic.main.item_task.view.*

class TaskListAdapter(val tasks: Task) : RecyclerView.Adapter<TaskListAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return 1 //TODO considering only one task will be available at a time
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.setData(tasks, position) //TODO get value from list in case of multiple tasks
    }

    /*override fun getItemViewType(position: Int): Int {
        val taskType = tasks.type
        return when (taskType) {
            "image" -> 1
            "audio" -> 2
            "mcq_radio" -> 3
            "mcq_checkbox" -> 4
            else -> 5 //TODO considering it 'text' type
        }
    }*/

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageTask = itemView.image_task
        val textTaskTitle = itemView.text_task_title
        val textTaskDescription = itemView.text_task_description
        val cardTask = itemView.card_task

        fun setData(task: Task, position: Int) {
            textTaskTitle.text = task.name
            textTaskDescription.text = task.type

            itemView.setOnClickListener {
                itemView.context.startActivity(Intent(itemView.context, TaskDetailsActivity::class.java))
            }
        }
    }
}